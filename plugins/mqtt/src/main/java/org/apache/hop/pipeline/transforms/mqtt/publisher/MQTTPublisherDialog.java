/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hop.pipeline.transforms.mqtt.publisher;

import org.apache.hop.core.Const;
import org.apache.hop.core.Props;
import org.apache.hop.core.exception.HopTransformException;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.util.Utils;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.transform.BaseTransformMeta;
import org.apache.hop.pipeline.transform.ITransformDialog;
import org.apache.hop.ui.core.dialog.BaseDialog;
import org.apache.hop.ui.core.dialog.ErrorDialog;
import org.apache.hop.ui.core.widget.TextVar;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

public class MQTTPublisherDialog extends BaseTransformDialog implements ITransformDialog {

  private MQTTPublisherMeta producerMeta;

  private CCombo wInputField;

  private TextVar wBroker;
  private Label wlTopicName;
  private CCombo wTopicName;
  private TextVar wClientID;
  private TextVar wTimeout;
  private TextVar wQOS;

  private Button wTopicFromIncomingField;

  private Button wRequiresAuth;
  private Label wlUsername;
  private TextVar wUsername;
  private Label wlPassword;
  private TextVar wPassword;

  private TextVar wCAFile;
  private TextVar wCertFile;
  private TextVar wKeyFile;
  private TextVar wKeyPassword;

  public MQTTPublisherDialog(
      Shell parent, IVariables variables, Object in, PipelineMeta tr, String transformname) {
    super(parent, variables, (BaseTransformMeta) in, tr, transformname);
    producerMeta = (MQTTPublisherMeta) in;
  }

  /*public MQTTPublisherDialog( Shell parent, BaseStepMeta baseStepMeta, TransMeta transMeta, String stepname ) {
    super( parent, baseStepMeta, transMeta, stepname );
    producerMeta = (MQTTPublisherMeta) baseStepMeta;
  }

  public MQTTPublisherDialog( Shell parent, int nr, BaseStepMeta in, TransMeta tr ) {
    super( parent, nr, in, tr );
    producerMeta = (MQTTPublisherMeta) in;
  }*/

  public String open() {
    Shell parent = getParent();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
    props.setLook(shell);
    setShellImage(shell, producerMeta);

    ModifyListener lsMod =
        new ModifyListener() {
          public void modifyText(ModifyEvent e) {
            producerMeta.setChanged();
          }
        };
    changed = producerMeta.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Shell.Title"));

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Step name
    wlTransformName = new Label(shell, SWT.RIGHT);
    wlTransformName.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.StepName.Label"));
    props.setLook(wlTransformName);
    fdlTransformName = new FormData();
    fdlTransformName.left = new FormAttachment(0, 0);
    fdlTransformName.right = new FormAttachment(middle, -margin);
    fdlTransformName.top = new FormAttachment(0, margin);
    wlTransformName.setLayoutData(fdlTransformName);
    wTransformName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wTransformName);
    wTransformName.addModifyListener(lsMod);
    fdTransformName = new FormData();
    fdTransformName.left = new FormAttachment(middle, 0);
    fdTransformName.top = new FormAttachment(0, margin);
    fdTransformName.right = new FormAttachment(100, 0);
    wTransformName.setLayoutData(fdTransformName);
    Control lastControl = wTransformName;

    // ====================
    // START OF TAB FOLDER
    // ====================
    CTabFolder wTabFolder = new CTabFolder(shell, SWT.BORDER);
    props.setLook(wTabFolder, Props.WIDGET_STYLE_TAB);

    // ====================
    // GENERAL TAB
    // ====================

    CTabItem wGeneralTab = new CTabItem(wTabFolder, SWT.NONE);
    wGeneralTab.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.GeneralTab.Label"));

    FormLayout mainLayout = new FormLayout();
    mainLayout.marginWidth = 3;
    mainLayout.marginHeight = 3;

    Composite wGeneralTabComp = new Composite(wTabFolder, SWT.NONE);
    props.setLook(wGeneralTabComp);
    wGeneralTabComp.setLayout(mainLayout);

    // Broker URL
    Label wlBroker = new Label(wGeneralTabComp, SWT.RIGHT);
    wlBroker.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Broker.Label"));
    props.setLook(wlBroker);
    FormData fdlBroker = new FormData();
    fdlBroker.top = new FormAttachment(0, margin * 2);
    fdlBroker.left = new FormAttachment(0, 0);
    fdlBroker.right = new FormAttachment(middle, -margin);
    wlBroker.setLayoutData(fdlBroker);
    wBroker = new TextVar(variables, wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wBroker);
    wBroker.addModifyListener(lsMod);
    FormData fdBroker = new FormData();
    fdBroker.top = new FormAttachment(0, margin * 2);
    fdBroker.left = new FormAttachment(middle, 0);
    fdBroker.right = new FormAttachment(100, 0);
    wBroker.setLayoutData(fdBroker);
    lastControl = wBroker;

    // Topic name
    wlTopicName = new Label(wGeneralTabComp, SWT.RIGHT);
    wlTopicName.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.TopicName.Label"));
    props.setLook(wlTopicName);
    FormData fdlTopicName = new FormData();
    fdlTopicName.top = new FormAttachment(lastControl, margin);
    fdlTopicName.left = new FormAttachment(0, 0);
    fdlTopicName.right = new FormAttachment(middle, -margin);
    wlTopicName.setLayoutData(fdlTopicName);
    wTopicName = new CCombo(wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wTopicName);
    wTopicName.addModifyListener(lsMod);
    FormData fdTopicName = new FormData();
    fdTopicName.top = new FormAttachment(lastControl, margin);
    fdTopicName.left = new FormAttachment(middle, 0);
    fdTopicName.right = new FormAttachment(100, 0);
    wTopicName.setLayoutData(fdTopicName);
    lastControl = wTopicName;

    // Topic is from field
    Label wlTopicIsFromField = new Label(wGeneralTabComp, SWT.RIGHT);
    wlTopicIsFromField.setText(
        BaseMessages.getString(
            MQTTPublisherMeta.PKG, "MQTTClientDialog.TopicNameIsInIncomingField.Label"));
    props.setLook(wlTopicIsFromField);
    FormData fd = new FormData();
    fd.top = new FormAttachment(lastControl, margin);
    fd.left = new FormAttachment(0, 0);
    fd.right = new FormAttachment(middle, -margin);
    wlTopicIsFromField.setLayoutData(fd);

    wTopicFromIncomingField = new Button(wGeneralTabComp, SWT.CHECK);
    props.setLook(wTopicFromIncomingField);
    wTopicFromIncomingField.addSelectionListener(
        new SelectionAdapter() {
          @Override
          public void widgetSelected(SelectionEvent selectionEvent) {
            super.widgetSelected(selectionEvent);
            producerMeta.setChanged();
            updateTopicCombo(getPreviousFields());
          }
        });
    fd = new FormData();
    fd.top = new FormAttachment(wlTopicIsFromField, 0, SWT.CENTER);
    fd.left = new FormAttachment(middle, 0);
    fd.right = new FormAttachment(100, 0);
    wTopicFromIncomingField.setLayoutData(fd);
    lastControl = wlTopicIsFromField;

    // Input field
    IRowMeta previousFields = getPreviousFields();

    Label wlInputField = new Label(wGeneralTabComp, SWT.RIGHT);
    wlInputField.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.FieldName.Label"));
    props.setLook(wlInputField);
    FormData fdlInputField = new FormData();
    fdlInputField.top = new FormAttachment(lastControl, 2 * margin);
    fdlInputField.left = new FormAttachment(0, 0);
    fdlInputField.right = new FormAttachment(middle, -margin);
    wlInputField.setLayoutData(fdlInputField);
    wInputField = new CCombo(wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wInputField.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.FieldName.Tooltip"));
    wInputField.setItems(previousFields.getFieldNames());
    props.setLook(wInputField);
    wInputField.addModifyListener(lsMod);
    FormData fdFilename = new FormData();
    fdFilename.top = new FormAttachment(wlInputField, 0, SWT.CENTER);
    fdFilename.left = new FormAttachment(middle, 0);
    fdFilename.right = new FormAttachment(100, 0);
    wInputField.setLayoutData(fdFilename);
    lastControl = wInputField;

    // Client ID
    Label wlClientID = new Label(wGeneralTabComp, SWT.RIGHT);
    wlClientID.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.ClientID.Label"));
    props.setLook(wlClientID);
    FormData fdlClientID = new FormData();
    fdlClientID.top = new FormAttachment(lastControl, margin);
    fdlClientID.left = new FormAttachment(0, 0);
    fdlClientID.right = new FormAttachment(middle, -margin);
    wlClientID.setLayoutData(fdlClientID);
    wClientID = new TextVar(variables, wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wClientID);
    wClientID.addModifyListener(lsMod);
    FormData fdClientID = new FormData();
    fdClientID.top = new FormAttachment(lastControl, margin);
    fdClientID.left = new FormAttachment(middle, 0);
    fdClientID.right = new FormAttachment(100, 0);
    wClientID.setLayoutData(fdClientID);
    lastControl = wClientID;

    // Connection timeout
    Label wlConnectionTimeout = new Label(wGeneralTabComp, SWT.RIGHT);
    wlConnectionTimeout.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.ConnectionTimeout.Label"));
    wlConnectionTimeout.setToolTipText(
        BaseMessages.getString(
            MQTTPublisherMeta.PKG, "MQTTClientDialog.ConnectionTimeout.ToolTip"));
    props.setLook(wlConnectionTimeout);
    FormData fdlConnectionTimeout = new FormData();
    fdlConnectionTimeout.top = new FormAttachment(lastControl, margin);
    fdlConnectionTimeout.left = new FormAttachment(0, 0);
    fdlConnectionTimeout.right = new FormAttachment(middle, -margin);
    wlConnectionTimeout.setLayoutData(fdlConnectionTimeout);
    wTimeout = new TextVar(variables, wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wTimeout);
    wTimeout.addModifyListener(lsMod);
    FormData fdConnectionTimeout = new FormData();
    fdConnectionTimeout.top = new FormAttachment(lastControl, margin);
    fdConnectionTimeout.left = new FormAttachment(middle, 0);
    fdConnectionTimeout.right = new FormAttachment(100, 0);
    wTimeout.setLayoutData(fdConnectionTimeout);
    lastControl = wTimeout;

    // QOS
    Label wlQOS = new Label(wGeneralTabComp, SWT.RIGHT);
    wlQOS.setText(BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.QOS.Label"));
    props.setLook(wlQOS);
    FormData fdlQOS = new FormData();
    fdlQOS.top = new FormAttachment(lastControl, margin);
    fdlQOS.left = new FormAttachment(0, 0);
    fdlQOS.right = new FormAttachment(middle, -margin);
    wlQOS.setLayoutData(fdlQOS);
    wQOS = new TextVar(variables, wGeneralTabComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wQOS);
    wQOS.addModifyListener(lsMod);
    FormData fdQOS = new FormData();
    fdQOS.top = new FormAttachment(lastControl, margin);
    fdQOS.left = new FormAttachment(middle, 0);
    fdQOS.right = new FormAttachment(100, 0);
    wQOS.setLayoutData(fdQOS);
    lastControl = wQOS;

    FormData fdGeneralTabComp = new FormData();
    fdGeneralTabComp.left = new FormAttachment(0, 0);
    fdGeneralTabComp.top = new FormAttachment(0, 0);
    fdGeneralTabComp.right = new FormAttachment(100, 0);
    fdGeneralTabComp.bottom = new FormAttachment(100, 0);
    wGeneralTabComp.setLayoutData(fdGeneralTabComp);

    wGeneralTabComp.layout();
    wGeneralTab.setControl(wGeneralTabComp);

    // ====================
    // CREDENTIALS TAB
    // ====================
    CTabItem wCredentialsTab = new CTabItem(wTabFolder, SWT.NONE);
    wCredentialsTab.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.CredentialsTab.Title"));

    Composite wCredentialsComp = new Composite(wTabFolder, SWT.NONE);
    props.setLook(wCredentialsComp);

    FormLayout fieldsCompLayout = new FormLayout();
    fieldsCompLayout.marginWidth = Const.FORM_MARGIN;
    fieldsCompLayout.marginHeight = Const.FORM_MARGIN;
    wCredentialsComp.setLayout(fieldsCompLayout);

    Label wlRequiresAuth = new Label(wCredentialsComp, SWT.RIGHT);
    wlRequiresAuth.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.RequireAuth.Label"));
    props.setLook(wlRequiresAuth);
    FormData fdlRequriesAuth = new FormData();
    fdlRequriesAuth.left = new FormAttachment(0, 0);
    fdlRequriesAuth.top = new FormAttachment(0, margin * 2);
    fdlRequriesAuth.right = new FormAttachment(middle, -margin);
    wlRequiresAuth.setLayoutData(fdlRequriesAuth);
    wRequiresAuth = new Button(wCredentialsComp, SWT.CHECK);
    props.setLook(wRequiresAuth);
    FormData fdRequiresAuth = new FormData();
    fdRequiresAuth.left = new FormAttachment(middle, 0);
    fdRequiresAuth.top = new FormAttachment(wlRequiresAuth, 0, SWT.CENTER);
    fdRequiresAuth.right = new FormAttachment(100, 0);
    wRequiresAuth.setLayoutData(fdRequiresAuth);
    wRequiresAuth.addSelectionListener(
        new SelectionAdapter() {
          public void widgetSelected(SelectionEvent arg0) {
            boolean enabled = wRequiresAuth.getSelection();
            wlUsername.setEnabled(enabled);
            wUsername.setEnabled(enabled);
            wlPassword.setEnabled(enabled);
            wPassword.setEnabled(enabled);
          }
        });
    lastControl = wlRequiresAuth;

    // Username field
    wlUsername = new Label(wCredentialsComp, SWT.RIGHT);
    wlUsername.setEnabled(false);
    wlUsername.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Username.Label"));
    props.setLook(wlUsername);
    FormData fdlUsername = new FormData();
    fdlUsername.left = new FormAttachment(0, -margin);
    fdlUsername.right = new FormAttachment(middle, -2 * margin);
    fdlUsername.top = new FormAttachment(lastControl, 2 * margin);
    wlUsername.setLayoutData(fdlUsername);

    wUsername = new TextVar(variables, wCredentialsComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wUsername.setEnabled(false);
    wUsername.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Username.Tooltip"));
    props.setLook(wUsername);
    wUsername.addModifyListener(lsMod);
    FormData fdResult = new FormData();
    fdResult.left = new FormAttachment(middle, -margin);
    fdResult.top = new FormAttachment(lastControl, 2 * margin);
    fdResult.right = new FormAttachment(100, 0);
    wUsername.setLayoutData(fdResult);
    lastControl = wUsername;

    // Password field
    wlPassword = new Label(wCredentialsComp, SWT.RIGHT);
    wlPassword.setEnabled(false);
    wlPassword.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Password.Label"));
    props.setLook(wlPassword);
    FormData fdlPassword = new FormData();
    fdlPassword.left = new FormAttachment(0, -margin);
    fdlPassword.right = new FormAttachment(middle, -2 * margin);
    fdlPassword.top = new FormAttachment(lastControl, margin);
    wlPassword.setLayoutData(fdlPassword);

    wPassword =
        new TextVar(variables, wCredentialsComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
    wPassword.setEnabled(false);
    wPassword.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.Password.Tooltip"));
    props.setLook(wPassword);
    wPassword.addModifyListener(lsMod);
    FormData fdPassword = new FormData();
    fdPassword.left = new FormAttachment(middle, -margin);
    fdPassword.top = new FormAttachment(lastControl, margin);
    fdPassword.right = new FormAttachment(100, 0);
    wPassword.setLayoutData(fdPassword);

    FormData fdCredentialsComp = new FormData();
    fdCredentialsComp.left = new FormAttachment(0, 0);
    fdCredentialsComp.top = new FormAttachment(0, 0);
    fdCredentialsComp.right = new FormAttachment(100, 0);
    fdCredentialsComp.bottom = new FormAttachment(100, 0);
    wCredentialsComp.setLayoutData(fdCredentialsComp);

    wCredentialsComp.layout();
    wCredentialsTab.setControl(wCredentialsComp);

    // ====================
    // SSL TAB
    // ====================
    CTabItem wSSLTab = new CTabItem(wTabFolder, SWT.NONE);
    wSSLTab.setText(BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.SSLTab.Label"));

    Composite wSSLComp = new Composite(wTabFolder, SWT.NONE);
    props.setLook(wSSLComp);

    FormLayout sslCompLayout = new FormLayout();
    sslCompLayout.marginWidth = Const.FORM_MARGIN;
    sslCompLayout.marginHeight = Const.FORM_MARGIN;
    wSSLComp.setLayout(sslCompLayout);

    // Server CA file path
    Label wlCAFile = new Label(wSSLComp, SWT.RIGHT);
    wlCAFile.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.CAFile.Label"));
    props.setLook(wlCAFile);
    FormData fdlCAFile = new FormData();
    fdlCAFile.left = new FormAttachment(0, -margin);
    fdlCAFile.right = new FormAttachment(middle, -2 * margin);
    fdlCAFile.top = new FormAttachment(0, 2 * margin);
    wlCAFile.setLayoutData(fdlCAFile);

    wCAFile = new TextVar(variables, wSSLComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wCAFile.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.CAFile.Tooltip"));
    props.setLook(wCAFile);
    wCAFile.addModifyListener(lsMod);
    FormData fdCAFile = new FormData();
    fdCAFile.left = new FormAttachment(middle, -margin);
    fdCAFile.top = new FormAttachment(0, 2 * margin);
    fdCAFile.right = new FormAttachment(100, 0);
    wCAFile.setLayoutData(fdCAFile);
    lastControl = wCAFile;

    // Client certificate file path
    Label wlCertFile = new Label(wSSLComp, SWT.RIGHT);
    wlCertFile.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.CertFile.Label"));
    props.setLook(wlCertFile);
    FormData fdlCertFile = new FormData();
    fdlCertFile.left = new FormAttachment(0, -margin);
    fdlCertFile.right = new FormAttachment(middle, -2 * margin);
    fdlCertFile.top = new FormAttachment(lastControl, margin);
    wlCertFile.setLayoutData(fdlCertFile);

    wCertFile = new TextVar(variables, wSSLComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wCertFile.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.CertFile.Tooltip"));
    props.setLook(wCertFile);
    wCertFile.addModifyListener(lsMod);
    FormData fdCertFile = new FormData();
    fdCertFile.left = new FormAttachment(middle, -margin);
    fdCertFile.top = new FormAttachment(lastControl, margin);
    fdCertFile.right = new FormAttachment(100, 0);
    wCertFile.setLayoutData(fdCertFile);
    lastControl = wCertFile;

    // Client key file path
    Label wlKeyFile = new Label(wSSLComp, SWT.RIGHT);
    wlKeyFile.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.KeyFile.Label"));
    props.setLook(wlKeyFile);
    FormData fdlKeyFile = new FormData();
    fdlKeyFile.left = new FormAttachment(0, -margin);
    fdlKeyFile.right = new FormAttachment(middle, -2 * margin);
    fdlKeyFile.top = new FormAttachment(lastControl, margin);
    wlKeyFile.setLayoutData(fdlKeyFile);

    wKeyFile = new TextVar(variables, wSSLComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wKeyFile.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.KeyFile.Tooltip"));
    props.setLook(wKeyFile);
    wKeyFile.addModifyListener(lsMod);
    FormData fdKeyFile = new FormData();
    fdKeyFile.left = new FormAttachment(middle, -margin);
    fdKeyFile.top = new FormAttachment(lastControl, margin);
    fdKeyFile.right = new FormAttachment(100, 0);
    wKeyFile.setLayoutData(fdKeyFile);
    lastControl = wKeyFile;

    // Client key file password path
    Label wlKeyPassword = new Label(wSSLComp, SWT.RIGHT);
    wlKeyPassword.setText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.KeyPassword.Label"));
    props.setLook(wlKeyPassword);
    FormData fdlKeyPassword = new FormData();
    fdlKeyPassword.left = new FormAttachment(0, -margin);
    fdlKeyPassword.right = new FormAttachment(middle, -2 * margin);
    fdlKeyPassword.top = new FormAttachment(lastControl, margin);
    wlKeyPassword.setLayoutData(fdlKeyPassword);

    wKeyPassword =
        new TextVar(variables, wSSLComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
    wKeyPassword.setToolTipText(
        BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.KeyPassword.Tooltip"));
    props.setLook(wKeyPassword);
    wKeyPassword.addModifyListener(lsMod);
    FormData fdKeyPassword = new FormData();
    fdKeyPassword.left = new FormAttachment(middle, -margin);
    fdKeyPassword.top = new FormAttachment(lastControl, margin);
    fdKeyPassword.right = new FormAttachment(100, 0);
    wKeyPassword.setLayoutData(fdKeyPassword);
    lastControl = wKeyPassword;

    FormData fdSSLComp = new FormData();
    fdSSLComp.left = new FormAttachment(0, 0);
    fdSSLComp.top = new FormAttachment(0, 0);
    fdSSLComp.right = new FormAttachment(100, 0);
    fdSSLComp.bottom = new FormAttachment(100, 0);
    wSSLComp.setLayoutData(fdSSLComp);

    wSSLComp.layout();
    wSSLTab.setControl(wSSLComp);

    // ====================
    // BUTTONS
    // ====================
    wOk = new Button(shell, SWT.PUSH);
    wOk.setText(BaseMessages.getString(MQTTPublisherMeta.PKG, "System.Button.OK"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(MQTTPublisherMeta.PKG, "System.Button.Cancel"));

    setButtonPositions(new Button[] {wOk, wCancel}, margin, null);

    // ====================
    // END OF TAB FOLDER
    // ====================
    FormData fdTabFolder = new FormData();
    fdTabFolder.left = new FormAttachment(0, 0);
    fdTabFolder.top = new FormAttachment(wTransformName, margin);
    fdTabFolder.right = new FormAttachment(100, 0);
    fdTabFolder.bottom = new FormAttachment(wOk, -margin);
    wTabFolder.setLayoutData(fdTabFolder);

    // Add listeners
    wCancel.addListener(SWT.Selection, e -> cancel());
    wOk.addListener(SWT.Selection, e -> ok());

    wTabFolder.setSelection(0);

    getData(producerMeta, true);
    producerMeta.setChanged(changed);

    BaseDialog.defaultShellHandling(shell, c -> ok(), c -> cancel());

    return transformName;
  }

  private IRowMeta getPreviousFields() {
    IRowMeta previousFields = null;

    try {
      previousFields = pipelineMeta.getPrevTransformFields(variables, transformName);
    } catch (HopTransformException e) {
      new ErrorDialog(
          shell,
          BaseMessages.getString(MQTTPublisherMeta.PKG, "System.Dialog.Error.Title"),
          BaseMessages.getString(
              MQTTPublisherMeta.PKG, "MQTTClientDialog.ErrorDialog.UnableToGetInputFields.Message"),
          e);
    }

    return previousFields;
  }

  private void updateTopicCombo(IRowMeta previousFields) {
    if (wTopicFromIncomingField.getSelection()) {
      wlTopicName.setText(
          BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.TopicNameFromField"));
    } else {
      wlTopicName.setText(
          BaseMessages.getString(MQTTPublisherMeta.PKG, "MQTTClientDialog.TopicName.Label"));
    }

    String current = wTopicName.getText();
    wTopicName.removeAll();
    if (wTopicFromIncomingField.getSelection()) {
      wTopicName.setItems(previousFields.getFieldNames());
    }

    if (!Utils.isEmpty(current)) {
      wTopicName.setText(current);
    }
  }

  /** Copy information from the meta-data input to the dialog fields. */
  private void getData(MQTTPublisherMeta producerMeta, boolean copyStepname) {
    if (copyStepname) {
      wTransformName.setText(transformName);
    }
    wBroker.setText(Const.NVL(producerMeta.getBroker(), ""));
    wTopicName.setText(Const.NVL(producerMeta.getTopic(), ""));
    wTopicFromIncomingField.setSelection(producerMeta.getTopicIsFromField());
    wInputField.setText(Const.NVL(producerMeta.getField(), ""));
    wClientID.setText(Const.NVL(producerMeta.getClientId(), ""));
    wTimeout.setText(Const.NVL(producerMeta.getTimeout(), "10000"));
    wQOS.setText(Const.NVL(producerMeta.getQoS(), "0"));

    wRequiresAuth.setSelection(producerMeta.isRequiresAuth());
    wRequiresAuth.notifyListeners(SWT.Selection, new Event());

    wUsername.setText(Const.NVL(producerMeta.getUsername(), ""));
    wPassword.setText(Const.NVL(producerMeta.getPassword(), ""));

    wCAFile.setText(Const.NVL(producerMeta.getSSLCaFile(), ""));
    wCertFile.setText(Const.NVL(producerMeta.getSSLCertFile(), ""));
    wKeyFile.setText(Const.NVL(producerMeta.getSSLKeyFile(), ""));
    wKeyPassword.setText(Const.NVL(producerMeta.getSSLKeyFilePass(), ""));

    updateTopicCombo(getPreviousFields());

    wTransformName.selectAll();
  }

  private void cancel() {
    transformName = null;
    producerMeta.setChanged(changed);
    dispose();
  }

  /** Copy information from the dialog fields to the meta-data input */
  private void setData(MQTTPublisherMeta producerMeta) {
    producerMeta.setBroker(wBroker.getText());
    producerMeta.setTopic(wTopicName.getText());
    producerMeta.setTopicIsFromField(wTopicFromIncomingField.getSelection());
    producerMeta.setField(wInputField.getText());
    producerMeta.setClientId(wClientID.getText());
    producerMeta.setTimeout(wTimeout.getText());
    producerMeta.setQoS(wQOS.getText());

    boolean requiresAuth = wRequiresAuth.getSelection();
    producerMeta.setRequiresAuth(requiresAuth);
    if (requiresAuth) {
      producerMeta.setUsername(wUsername.getText());
      producerMeta.setPassword(wPassword.getText());
    }

    producerMeta.setSSLCaFile(wCAFile.getText());
    producerMeta.setSSLCertFile(wCertFile.getText());
    producerMeta.setSSLKeyFile(wKeyFile.getText());
    producerMeta.setSSLKeyFilePass(wKeyPassword.getText());

    producerMeta.setChanged();
  }

  private void ok() {
    if (Utils.isEmpty(wTransformName.getText())) {
      return;
    }
    setData(producerMeta);
    transformName = wTransformName.getText();
    dispose();
  }
}
