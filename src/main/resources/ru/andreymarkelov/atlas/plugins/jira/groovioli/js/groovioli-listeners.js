AJS.toInit(function () {
    new AJS.MultiSelect({
        element: jQuery("#projects"),
        itemAttrDisplayed: "label",
        showDropdownButton: true
    });

    new AJS.MultiSelect({
        element: jQuery("#events"),
        itemAttrDisplayed: "label",
        showDropdownButton: true
    });
});
