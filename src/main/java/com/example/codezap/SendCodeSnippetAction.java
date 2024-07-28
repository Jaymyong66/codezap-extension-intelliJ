package com.example.codezap;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendCodeSnippetAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor != null) {
                SelectionModel selectionModel = editor.getSelectionModel();
                String selectedText = selectionModel.getSelectedText();

                if (selectedText != null && !selectedText.isEmpty()) {
                    String title = Messages.showInputDialog("Enter the title for your code snippet:", "Input Title", Messages.getQuestionIcon());
                    String filename = Messages.showInputDialog("Enter the filename for your code snippet:", "Input Filename", Messages.getQuestionIcon());

                    if (title != null && filename != null) {
                        try {
                            sendCodeSnippetToApi(title, filename, selectedText);
                            Messages.showInfoMessage("Code snippet successfully sent to CodeZap API!", "Success");
                        } catch (IOException e) {
                            Messages.showErrorDialog("Failed to send code snippet: " + e.getMessage(), "Error");
                        }
                    } else {
                        Messages.showErrorDialog("Title and filename are required!", "Error");
                    }
                } else {
                    Messages.showErrorDialog("No code selected!", "Error");
                }
            } else {
                Messages.showErrorDialog("No active editor found!", "Error");
            }
        }
    }

    private void sendCodeSnippetToApi(String title, String filename, String content) throws IOException {
        URL url = new URL("코드잽 api/templates 주소!!");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = String.format("{\"title\": \"%s\", \"snippets\": [{\"filename\": \"%s\", \"content\": \"%s\", \"ordinal\": 1}]}", title, filename, content);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to send code snippet, response code: " + responseCode);
        }
    }
}
