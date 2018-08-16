AJS.toInit(function () {
    AJS.$("#projectId").auiSelect2();
    AJS.$("#eventType").auiSelect2();

    AJS.$(".grovioli-delete-listener").click(function (e) {
        if (confirm("Are you sure?")) {
            AJS.$("#groovioli-listener-deleteform > #listenerId").val(AJS.$(this).attr("data-listenerId"));
            AJS.$("#groovioli-listener-deleteform").submit();
        }
    });

    AJS.$(".grovioli-view-listener").click(function (e) {
        alert("view");
    });
});
