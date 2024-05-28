<#include "layout.ftl">

<#macro content>
<div class="container">
    <h2>Compose Email</h2>
    <form action="/sendEmails" method="POST">
        <div class="form-group">
            <label for="spreadSheetId">Spreadsheet ID</label>
            <input type="text" class="form-control" id="spreadSheetId" name="spreadSheetId" required>
        </div>
        <div class="form-group">
            <label for="subject">Subject</label>
            <input type="text" class="form-control" id="subject" name="subject" value="Grade Notification for {studentName}" required>
        </div>
        <div class="form-group">
            <label for="body">Body</label>
            <div id="toolbar">
                <#list existingTables?keys as tableName>
                    <button type="button" class="btn btn-secondary" onclick="insertPlaceholder('${tableName}')">${tableName}</button>
                </#list>
            </div>
            <textarea class="form-control" id="body" name="body" rows="5" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Send Email</button>
    </form>
</div>

<script>
    function insertPlaceholder(tableName) {
        const textarea = document.getElementById('body');
        const cursorPos = textarea.selectionStart;
        const textBefore = textarea.value.substring(0, cursorPos);
        const textAfter  = textarea.value.substring(cursorPos, textarea.value.length);
        textarea.value = textBefore + '{' + tableName + '}' + textAfter;
    }
</script>
</#macro>
