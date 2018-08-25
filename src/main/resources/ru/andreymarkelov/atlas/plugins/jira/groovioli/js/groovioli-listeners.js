AJS.toInit(function () {
    AJS.$("#projectId").auiSelect2();
    AJS.$("#eventType").auiSelect2();

    AJS.$(".grovioli-delete-listener").click(function (e) {
        if (confirm("Are you sure?")) {
            AJS.$("#groovioli-listener-deleteform > #listenerId").val(AJS.$(this).attr("data-listenerId"));
            AJS.$("#groovioli-listener-deleteform").submit();
        }
    });

    function viewListener(listenerId) {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/groovioli/1.0/listener/getscript?listenerId=" + listenerId,
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

    AJS.$(".grovioli-view-listener").click(function (e) {
        viewListener(AJS.$(this).attr("data-listenerId"));
    });

    AJS.$("#groovioli-script-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#groovioli-script-dialog").hide();
    });
});
