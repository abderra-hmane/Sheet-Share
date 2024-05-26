<#include "layout.ftl">

<#macro content>
<div class="container">
    <h2>Customize and Send Emails</h2>
    <form action="/sendEmails" method="post">
        <div class="form-group">
            <label for="spreadSheetId">Spreadsheet ID</label>
            <input type="text" class="form-control" id="spreadSheetId" name="spreadSheetId" required>
        </div>
        <div class="form-group">
            <label for="subjectTemplate">Subject Template</label>
            <input type="text" class="form-control" id="subjectTemplate" name="subjectTemplate" value="Grade Notification for {studentName}" required>
        </div>
        <div class="form-group">
            <label for="bodyTemplate">Body Template</label>
            <textarea class="form-control" id="bodyTemplate" name="bodyTemplate" rows="5" required>
Hello {studentName},

Your grade is: {grade}.

Best regards,
Your Professor
            </textarea>
        </div>
        <button type="submit" class="btn btn-primary">Send Emails</button>
    </form>
</div>
</#macro>
