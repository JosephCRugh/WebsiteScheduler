function addTime(event) {
    event.preventDefault();
    let form = $('#add-form');
    $.ajax({
        type: "POST",
        url: form.attr('action'),
        data: form.serialize(),
        success: function(response) {
            if ($(response).find('#error-display').length) {
                form.replaceWith(response);
            } else {
                $('#time-sheet').replaceWith(response);
            }
        }
    });
}

function deleteTime(event) {
    let timeId = $(event.target).attr('value');
    $.ajax({
        type: "POST",
        url: "/schedule/create/remove/" + timeId,
        success: function(response) {
            $('#time-sheet').replaceWith(response);
        }
    });
}