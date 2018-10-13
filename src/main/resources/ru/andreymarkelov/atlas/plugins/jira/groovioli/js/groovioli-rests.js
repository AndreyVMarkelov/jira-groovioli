AJS.toInit(function () {
    AJS.$(".grovioli-delete-rest").click(function (e) {
        if (confirm("Are you sure?")) {
            AJS.$("#groovioli-rest-deleteform > #restDataId").val(AJS.$(this).attr("data-restid"));
            AJS.$("#groovioli-rest-deleteform").submit();
        }
    });

    function viewRest(restDataId) {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/groovioli/1.0/rest/getscript?restDataId=" + restDataId,
            type: "GET",
            contentType: "text/plain",
            processData: false,
            success: function (e) {
                AJS.$("#script-in-dialog").val(e);
                AJS.dialog2("#groovioli-script-dialog").show();
            },
            error: function (error) {
                if (error.responseText) {
                    alert(error.responseText);
                }
            }
        });
    }

    AJS.$(".grovioli-view-rest").click(function (e) {
        viewRest(AJS.$(this).attr("data-restid"));
    });

    AJS.$("#groovioli-script-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#groovioli-script-dialog").hide();
    });
});
