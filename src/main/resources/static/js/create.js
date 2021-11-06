function addTime(event) {
    event.preventDefault();
    let form = $('#add-form');
    console.log("Sending over data!!")
    console.log("url: " + form.attr('action'));
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