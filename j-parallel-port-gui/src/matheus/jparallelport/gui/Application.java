package matheus.jparallelport.gui;

import java.io.IOException;

import matheus.jparallelport.JParallelPort;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Application {
	static JParallelPort parport = new JParallelPort();
	private static GUI gui;
	private static Display display;
	private static Shell shell;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			display = new Display();
			shell = new Shell(display);
			parport.open();

			// Ensure to terminate refresh thread (GUI)
			// before exit
			shell.addShellListener(new ShellListener() {

				@Override
				public void shellIconified(ShellEvent arg0) {

				}

				@Override
				public void shellDeiconified(ShellEvent arg0) {

				}

				@Override
				public void shellDeactivated(ShellEvent arg0) {

				}

				@Override
				public void shellClosed(ShellEvent arg0) {
					System.out.println("shellClosed event");
					gui.refreshThread.terminate();
				}

				@Override
				public void shellActivated(ShellEvent arg0) {

				}
			});

			// Menu
			Menu menu = new Menu(shell, SWT.BAR);

			MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
			fileItem.setText("File");
			Menu fileMenu = new Menu(menu);
			fileItem.setMenu(fileMenu);
			MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
			exitItem.setText("Exit");
			exitItem.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event arg0) {
					shell.close();
				}
			});

			MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
			helpItem.setText("Help");
			Menu helpMenu = new Menu(menu);
			helpItem.setMenu(helpMenu);
			MenuItem aboutItem = new MenuItem(helpMenu, SWT.NONE);
			aboutItem.setText("About");
			aboutItem.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					MessageBox messageBox = new MessageBox(shell,
							SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("About");
					messageBox
							.setMessage("JParallelPort GUI, JParallelPort (Java) and ParallelPort (C++)\n"
									+ "was written by me:\n\n"
									+ "Matheus Neder <matheusneder@gmail.com>\n\n"
									+ "I wish to share it, probably on google code, soon.\n\n"
									+ "ParallelPort depends on parport (oficial linux lpt driver)\n"
									+ "in Linux Systems and inpout32 on Win32 Systems.\n\n"
									+ "JParallelPort depends on ParallelPort and JParallelPort GUI \n"
									+ "depends on JParallelPort, Java and SWT.");
					messageBox.open();
				}
			});
			shell.setMenuBar(menu);
			// end Menu

			shell.setText("JParallelPort GUI");
			shell.setLayout(new FillLayout());
			gui = new GUI(shell, SWT.BORDER_SOLID);

			shell.pack();
			shell.setMinimumSize(shell.getSize());
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		} catch (IOException e) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR
					| SWT.OK);
			messageBox.setText("I/O Error");
			messageBox.setMessage(e.getMessage() + " !");
			messageBox.open();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		} finally {
			if (parport.isOpen()) {
				parport.close();
				System.out.println("JParallelPort closed!");
			}
		}

	}

}