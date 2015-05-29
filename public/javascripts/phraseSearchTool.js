(function(){

    "use strict";

    var tableHtml = '<table id=\"search-results-table\" class="table table-bordered"><tr><th width=\"30%\">Id</th><th width=\"20%\">Title</th><th width=\"20%\">Score</th>'
    var previous, next = null;
    var range = 20;

    $('#search-form').submit(function(evt){
        evt.preventDefault();
        var query = $('#fullStringParameter.form-control').val();
        ajaxRequest({
            query: query
        });
    });

    $('body').on('click','#previous',function (evt) {
        evt.preventDefault();
        ajaxRequest(previous);
    });
    $('body').on('click','#next',function (evt) {
        evt.preventDefault();
        ajaxRequest(next);
    });

    function ajaxRequest(params) {
        $.ajax({
            url: '/search/phrase',
            type: 'POST',
            contentType: 'application/json',
            data: '{"query": "'+ params.query+'"}',
            success: updatePage,
            error: function(xhr, err){
                alert('Error: ' + err + ' for call: ' + xhr);
            }
        });
    }

    function updatePage(searchResults){
        var pagination = $(paginationData(searchResults));
        var searchData = createSearchData(searchResults);
        var table = $(tableHtml).append(searchData);
        replaceData(pagination, table);
    }

    function paginationData(data) {
        return '<div id=\"no-of-results\"><h4>Number of results: ' + data.length + '</h4>';
    }

    function createSearchData(results) {
        var tableData = '';
        results.forEach(function(data) {
            tableData += newRow(data);
        });
        return tableData;
    }

    function newRow(data) {
        var twoRow = '<tr><td class=\"time\">' + data.document.id + '</td><td>' + data.document.title + '</td><td>' + data.score + '</td></tr>' +
            '<tr><td>' + data.document.content + '</td></tr>'
        return twoRow
    }

    function replaceData(pagination, table) {
        $('#no-of-results').empty();
        $('#search-results').empty();
        $('#no-of-results').append(pagination)
        $('#search-results').append(table);
    }

})();