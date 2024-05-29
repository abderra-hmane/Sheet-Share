<#include "layout.ftl">

<#macro content>
<div class="container">
    <h2>Compose Email</h2>
    <form action="/sendEmails" method="POST">
        <!-- Email Table Selection -->
        <div class="form-group">
            <label for="emailTable">Select Emails Table To Send</label>
            <select class="form-control" id="emailTable" name="emailTable">
                <#list existingTables?keys as tableName>
                    <option value="${tableName}">${tableName}</option>
                </#list>
            </select>
        </div>

        <!-- Subject Section -->
        <div class="form-group">
            <label for="subject">Subject</label>
            <input type="text" class="form-control" id="subject" name="subject" value="Grade Notification for " required>
            <div id="subjectToolbar" class="mt-2">
                <label>Insert placeholder into subject:</label>
                <#list existingTables?keys as tableName>
                    <button type="button" class="btn btn-secondary" onclick="insertPlaceholder('${tableName}', 'subject')">${tableName}</button>
                </#list>
            </div>
        </div>

        <!-- Body Section -->
        <div class="form-group">
            <label for="body">Body</label>
            <textarea class="form-control" id="body" name="body" rows="5" required></textarea>
            <div id="bodyToolbar" class="mt-2">
                <label>Insert placeholder into body:</label>
                <#list existingTables?keys as tableName>
                    <button type="button" class="btn btn-secondary" onclick="insertPlaceholder('${tableName}', 'body')">${tableName}</button>
                </#list>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Send Email</button>
    </form>
</div>

<script>
    function insertPlaceholder(tableName, field) {
        const textarea = document.getElementById(field);
        const cursorPos = textarea.selectionStart;
        const textBefore = textarea.value.substring(0, cursorPos);
        const textAfter = textarea.value.substring(cursorPos, textarea.value.length);
        textarea.value = textBefore + '{' + tableName + '}' + textAfter;
    }
</script>
</#macro>
