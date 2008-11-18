package org.pihen.facebook.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;
import org.pihen.facebook.dao.FacebookJaxBDaoImpl;

public class Log4JFrame extends JXPanel {

	/** Logger for this class. */
	private static Logger _log = Logger.getLogger(FacebookJaxBDaoImpl.class);
	/** Combo box containing all the log files. */
	/** Text area containing the log contents. */
	private final JTextArea _logContentsTxt = new JTextArea(20, 50);
	/** Button that refreshes the log contents. */
	private final JButton _refreshBtn = new JButton("Rafraichir");
	private final JCheckBox _errorChkbox = new JCheckBox("Errors");
	private final JCheckBox _debugChkbox = new JCheckBox("Debug");
	private final JCheckBox _infoChkbox = new JCheckBox("Info");
	/** Directory containing the log files. */
	private final File _logDir;
	/** If <TT>true</TT> user is closing this window. */
	private boolean _closing = false;
	/** If <TT>true</TT> log is being refreshed. */
	private boolean _refreshing = false;
	/** Size of current log file */
	private long _fileLenght;
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if a <TT>null</TT> <TT>IApplication passed.
	 */
	public Log4JFrame() {
		_logDir = new File("fbswing.log"); // !!! mettez ici votre fichier
		_fileLenght = _logDir.length();

		createUserInterface();
		startRefreshingLog();
		new Thread(new LenghtDetect()).start();
	}

	/**
	 * Start a thread to refrsh the log.
	 */
	private synchronized void startRefreshingLog() {
		if (!_refreshing) {

			new Thread(new Refresher()).run();

		}
		// refreshLog();
	}

	/**
	 * Enables the log combo box and refresh button using the Swing event
	 * thread.
	 */
	private void enableComponents(final boolean enabled) {
		Runnable todo = new Runnable() {

			public void run() {
				_refreshBtn.setEnabled(enabled);
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			todo.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(todo);
			} catch (InterruptedException ex) {
				_log.error(ex);
			} catch (InvocationTargetException ex) {
				_log.error(ex);
			}
		}
	}

	/**
	 * Refresh the log.
	 */
	private void refreshLog() {
		enableComponents(false);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			_logContentsTxt.setText("");

			final File logFile = _logDir;
			if (logFile != null) {
				try {
					if (logFile.exists() && logFile.canRead()) {
						final BufferedReader rdr = new BufferedReader(
								new FileReader(logFile));
						try {
							String line = null;
							StringBuffer chunk = new StringBuffer(16384);
							while ((line = rdr.readLine()) != null) {
								if (_closing) {
									return;
								}

								if (chunk.length() > 16000) {
									final String finalLine = chunk.toString();

									if (!_closing) {
										_logContentsTxt.append(finalLine);
									}

									chunk = new StringBuffer(16384);
								} else {
									if (shouldAppendLineToChunk(line)) {
										chunk.append(line).append('\n');
									}
								}
							}

							if (_closing) {
								return;
							}

							final String finalLine = chunk.toString();
							if (!_closing) {
								_logContentsTxt.append(finalLine);
							}

						} finally {
							rdr.close();
						}
					}
				} catch (Exception ex) {
					// i18n[ViewLogsSheet.error.processinglogfile=Error occured
					// processing log file]
					final String msg = "ViewLogsSheet.error.processinglogfile";
					_log.error(msg, ex);
				}
			} else {
				// i18n[ViewLogsSheet.info.nulllogfile=Null log file name]
				_log.debug("ViewLogsSheet.info.nulllogfile");
			}

			if (_closing) {
				return;
			}

			// Position to the start of the last line in log.
			try {
				int pos = Math.max(0, _logContentsTxt.getText().length() - 1);
				int line = _logContentsTxt.getLineOfOffset(pos);
				final int finalpos = _logContentsTxt.getLineStartOffset(line);
				_logContentsTxt.setCaretPosition(finalpos);

			} catch (Exception ex) {
				// i18n[ViewLogsSheet.error.setcaret=Error positioning caret in
				// log text component]
				_log.error("ViewLogsSheet.error.setcaret", ex);
			}
		} finally {
			enableComponents(true);
			_refreshing = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * 199828 [Foo Thread] message
	 * 
	 * @param line
	 * @return
	 */
	private boolean shouldAppendLineToChunk(String line) {
		boolean result = false;
		if (line == null || line.length() == 0) {
			return false;
		}
		if (_errorChkbox.isSelected() && _debugChkbox.isSelected()
				&& _infoChkbox.isSelected()) {
			return true;
		}
		int threadNameEndIdx = line.indexOf("]");
		if (threadNameEndIdx > -1) {
			if (threadNameEndIdx + 2 >= line.length()) {
				return false;
			} // Garde de fou
			char levelChar = line.charAt(threadNameEndIdx + 2);
			if (_errorChkbox.isSelected() && levelChar == 'E') {
				result = true;
			}
			if (_debugChkbox.isSelected() && levelChar == 'D') {
				result = true;
			}
			if (_infoChkbox.isSelected() && levelChar == 'I') {
				result = true;
			}
			if (levelChar != 'E' && levelChar != 'D' && levelChar != 'I') {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * Create user interface.
	 */
	private void createUserInterface() {
		// putClientProperty("JInternalFrame.isPalette", true);
		setLayout(new BorderLayout());
		// contentPane.add(createToolBar(), BorderLayout.NORTH);
		add(createMainPanel(), BorderLayout.CENTER);
		add(createButtonsPanel(), BorderLayout.SOUTH);
	}

	/**
	 * Create the main panel containing the log details and selector.
	 */
	private JPanel createMainPanel() {

		// File appLogFile = new ApplicationFiles().getExecutionLogFile();
		final JPanel pnl = new JPanel(new BorderLayout());
		_logContentsTxt.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
		pnl.add(new JScrollPane(_logContentsTxt), BorderLayout.CENTER);

		return pnl;
	}

	/**
	 * Create panel at bottom containing the buttons.
	 */
	private JPanel createButtonsPanel() {
		JPanel pnl = new JPanel();

		pnl.add(_refreshBtn);
		_refreshBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				startRefreshingLog();
			}
		});

		_errorChkbox.setSelected(true);

		_debugChkbox.setSelected(true);
		_infoChkbox.setSelected(true);
		ActionListener changeLogListener = new ChangeLogListener();
		_errorChkbox.addActionListener(changeLogListener);
		_debugChkbox.addActionListener(changeLogListener);
		_infoChkbox.addActionListener(changeLogListener);
		pnl.add(_errorChkbox);
		pnl.add(_infoChkbox);
		pnl.add(_debugChkbox);
		// GUIUtilities.setJButtonSizesTheSame(new JButton[]{closeBtn,
		// _refreshBtn});

		return pnl;
	}

	private final class ChangeLogListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			Log4JFrame.this.startRefreshingLog();
		}
	}

	private final class Refresher implements Runnable {

		public void run() {
			// ViewLogsSheet.this.
			refreshLog();
		}
	}

	private class LenghtDetect implements Runnable {

		public void run() {
			while (!_closing) {
				try {
					Thread.sleep(1000);
					if (_fileLenght < _logDir.length()) {
						if (!_refreshing)
							refreshLog(); // doit être exécuter sur le même
											// thread
						_fileLenght = _logDir.length();
					}
				} catch (InterruptedException ex) {
					_log.error("Erreur Thread lecture console", ex);
				}
			}
		}
	}

}
