package org.herac.tuxguitar.gui.tools.browser.ftp;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.tools.browser.TGBrowserCollection;
import org.herac.tuxguitar.gui.tools.browser.TGBrowserManager;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserData;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserFactory;
import org.herac.tuxguitar.gui.util.DialogUtils;
import org.herac.tuxguitar.gui.util.MessageDialog;

public class TGBrowserFactoryImpl implements TGBrowserFactory{
	
	public TGBrowserFactoryImpl() {
		super();
	}
	
	public String getType(){
		return "ftp";
	}
	
	public String getName(){
		return "FTP";
	}
	
	public TGBrowser newTGBrowser(TGBrowserData data) {
		if(data instanceof TGBrowserDataImpl){
			return new TGBrowserImpl((TGBrowserDataImpl)data);
		}
		return null;
	}
	
	public TGBrowserData parseData(String string) {
		return TGBrowserDataImpl.fromString(string);
	}
	
	public TGBrowserData dataDialog(Shell parent) {
		return new TGBrowserDataDialog().show(parent);
	}
	
}
class TGBrowserDataDialog{
	
	protected TGBrowserDataImpl data;
	
	public TGBrowserDataImpl show(final Shell parent){
		final Shell dialog = DialogUtils.newDialog(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
		dialog.setLayout(new GridLayout());
		dialog.setText(TuxGuitar.getProperty("FTP Location"));
		
		//-------------LIBRARY DATA-----------------------------------------------
		Composite composite = new Composite(dialog, SWT.NONE);
		composite.setLayout(new GridLayout(2,false));
		composite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		GridData textData = new GridData(SWT.FILL,SWT.FILL,true,true);
		textData.minimumWidth = 300;
		
		//name
		Label nameLabel = new Label(composite, SWT.NULL);
		nameLabel.setText(TuxGuitar.getProperty("Name"));
		final Text nameText = new Text(composite,SWT.BORDER);
		nameText.setLayoutData(textData);
		
		
		//host
		Label hostLabel = new Label(composite, SWT.NULL);
		hostLabel.setText(TuxGuitar.getProperty("Host"));
		final Text hostText = new Text(composite,SWT.BORDER);
		hostText.setLayoutData(textData);
		
		//path
		Label pathLabel = new Label(composite, SWT.NULL);
		pathLabel.setText(TuxGuitar.getProperty("Path"));
		final Text pathText = new Text(composite,SWT.BORDER);
		pathText.setLayoutData(textData);
		
		//user
		Label userLabel = new Label(composite, SWT.NULL);
		userLabel.setText(TuxGuitar.getProperty("Login name"));
		final Text userText = new Text(composite,SWT.BORDER);
		userText.setLayoutData(textData);
		
		//password
		Label passwordLabel = new Label(composite, SWT.NULL);
		passwordLabel.setText(TuxGuitar.getProperty("Password"));
		final Text passwordText = new Text(composite,SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(textData);
		
		// Proxy
		final Button hasProxy = new Button(composite,SWT.CHECK);
		hasProxy.setText("Connect via Proxy Server");
		Label dummyLabel = new Label(composite, SWT.NULL);
		dummyLabel.setLayoutData(textData);
		
		//proxy host
		final Label proxyHostLabel = new Label(composite, SWT.NULL);
		proxyHostLabel.setText(TuxGuitar.getProperty("Proxy Server Host"));
		final Text proxyHostText = new Text(composite,SWT.BORDER);
		proxyHostText.setLayoutData(textData);
		
		//proxy port
		final Label proxyPortLabel = new Label(composite, SWT.NULL); 
		proxyPortLabel.setText(TuxGuitar.getProperty("Proxy Server Port"));
		final Text proxyPortText = new Text(composite,SWT.BORDER);
		proxyPortText.setText("1080");
		proxyPortText.setLayoutData(textData);
		
		//proxy user
		final Label proxyUserLabel = new Label(composite, SWT.NULL);
		proxyUserLabel.setText(TuxGuitar.getProperty("Proxy Server User"));
		final Text proxyUserText = new Text(composite,SWT.BORDER);
		proxyUserText.setLayoutData(textData);
		
		//proxy password
		final Label proxyPwdLabel = new Label(composite, SWT.NULL);
		proxyPwdLabel.setText(TuxGuitar.getProperty("Proxy Server Password"));
		final Text proxyPwdText = new Text(composite,SWT.BORDER | SWT.PASSWORD);
		proxyPwdText.setLayoutData(textData);
		
		proxyHostText.setEnabled(false);
		proxyPortText.setEnabled(false);
		proxyUserText.setEnabled(false);
		proxyPwdText.setEnabled(false);
		
		hasProxy.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				proxyHostText.setEnabled(hasProxy.getSelection());
				proxyPortText.setEnabled(hasProxy.getSelection());
				proxyUserText.setEnabled(hasProxy.getSelection());
				proxyPwdText.setEnabled(hasProxy.getSelection());
			}
		});
	
		
		//------------------BUTTONS--------------------------
		Composite buttons = new Composite(dialog, SWT.NONE);
		buttons.setLayout(new GridLayout(2,false));
		buttons.setLayoutData(new GridData(SWT.END,SWT.FILL,true,true));
		
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		data.minimumWidth = 80;
		data.minimumHeight = 25;
		
		final Button buttonOk = new Button(buttons, SWT.PUSH);
		buttonOk.setText(TuxGuitar.getProperty("ok"));
		buttonOk.setLayoutData(data);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				String name = nameText.getText();
				String host = hostText.getText();
				String path = pathText.getText();
				String user = userText.getText();
				String password = passwordText.getText();
				String proxyHost = proxyHostText.getText();
				String proxyPortStr = proxyPortText.getText();
				String proxyUser = proxyUserText.getText();
				String proxyPwd = proxyPwdText.getText();
				int proxyPort = -1;
				String err = null;
				if (name != null && name.trim().length() > 0) {
					Iterator it = TGBrowserManager.instance().getCollections();
					while(it.hasNext()){
						TGBrowserCollection collection = (TGBrowserCollection)it.next();
						if("ftp".equals(collection.getType())){
							if(name.equals(collection.getData().getTitle())){
								err = "A collection named \""+ name+"\" already exists";
								break;
							}
						}
					}
					if(err == null){
						if (host != null && host.trim().length() > 0) {
							if(hasProxy.getSelection()){
								if(proxyHost == null || proxyHost.trim().length() == 0){
									err = "Please enter Proxy Host";
									proxyHost = null;
								}
								if(proxyPortStr == null || proxyPortStr.trim().length() == 0){
									if(err != null)
										err += " and ";
									else
										err = "Please enter ";
									err += "Proxy Port";
								}else{
									try {
										proxyPort = Integer.parseInt(proxyPortStr);
									} catch (NumberFormatException e) {
										if(err != null)
											err += System.getProperty("line.separator");
										else
											err = "";
										err += "Proxy Port should be a valid number";
									}
								}
							}
							if(err == null){
								TGBrowserDataDialog.this.data = new TGBrowserDataImpl(name, host, path, user, password, proxyUser, proxyPwd, proxyHost, proxyPort);
							}
						}else{
							err = "Please enter the Host";
						}
					}
				}else{
					err = "Please enter the Name";
				}
				if(err == null)
					dialog.dispose();
				else
					MessageDialog.errorMessage(parent, err);
			}
		});
		
		Button buttonCancel = new Button(buttons, SWT.PUSH);
		buttonCancel.setLayoutData(data);
		buttonCancel.setText(TuxGuitar.getProperty("cancel"));
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				dialog.dispose();
			}
		});
		
		dialog.setDefaultButton( buttonOk );
		
		DialogUtils.openDialog(dialog,DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
		
		return this.data;
	}
}