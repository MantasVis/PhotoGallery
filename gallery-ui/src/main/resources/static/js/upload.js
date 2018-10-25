function getFileName() {
    var label = document.getElementById("inputfileLabel");
    var fullPath = document.getElementById("inputfile").value;
    if (fullPath) {
        var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
        var filename = fullPath.substring(startIndex);
        if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
            filename = filename.substring(1);
        }
        label.innerHTML = filename;
    }
}