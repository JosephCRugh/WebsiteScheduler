function timeSelected(event) {
    // For whatever reason the event wants to not provide
    // a way to get the div with onclick attribute so
    // just going up the DOM to find the div
    let prim = $(event.target);
    while (prim.attr('name') != "time-box") {
        prim = prim.parent();
    }

    $("div[name='time-box']").each(function(box) {
        $(this).removeClass('btn-primary');
    });

    prim.addClass('btn-primary');

    // Storing the time id for sending off to the server
    $('#time-sheet').attr('value', prim.attr('value'));

    $('#confirm-button').show();
}

function confirmSchedule(event) {
    let btn = $('#confirm-button');
    let timeSheet = $('#time-sheet');
    window.location.replace("/schedule/confirm-schedule-select/" + btn.attr('value') + "/" + timeSheet.attr('value'));
}