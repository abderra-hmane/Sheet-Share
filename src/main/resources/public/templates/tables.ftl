<#include "layout.ftl">

<#macro content>
<div class="container">
    <h2>Manage Tables</h2>
    <form action="/createTable" method="post">
        <div class="form-group">
            <label for="tableName">Table Name</label>
            <input type="text" class="form-control" id="tableName" name="tableName" required>
        </div>
        <div class="form-group">
            <label for="spreadSheetId">Spreadsheet ID</label>
            <input type="text" class="form-control" id="spreadSheetId" name="spreadSheetId" required>
        </div>
        <div class="form-group">
            <label for="sheetName">Sheet Name</label>
            <input type="text" class="form-control" id="sheetName" name="sheetName" required>
        </div>
        <div class="form-group">
            <label for="range">Range</label>
            <input type="text" class="form-control" id="range" name="range" required>
        </div>
        <button type="submit" class="btn btn-primary">Create Table</button>
    </form>

    <h3>Existing Tables</h3>
    <#if existingTables?has_content>
        <#list existingTables as tableName, tableData>
            <h4>${tableName}</h4>
            <table class="table">
                <thead>
                    <tr>
                        <#list tableData[0] as header>
                            <th>${header}</th>
                        </#list>
                    </tr>
                </thead>
                <tbody>
                    <#list tableData[1..] as row>
                        <tr>
                            <#list row as cell>
                                <td>${cell}</td>
                            </#list>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </#list>
    <#else>
        <p>No tables created yet.</p>
    </#if>
</div>
</#macro>