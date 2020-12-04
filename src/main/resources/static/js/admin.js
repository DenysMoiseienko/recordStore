$('.delete').click(function () {
    var res = confirm('Confirm action');
    if (!res) return false;
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#image").attr("src", e.target.result).width(200);
        }
        reader.readAsDataURL(input.files[0]);
    }
}

$("#data").change(function () {
    readURL(this);
})