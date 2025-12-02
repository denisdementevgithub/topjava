const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "date_time"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function getFilterForm() {
    $("#filterForm").modal();
}

function doFilter() {
    $('#filterForm').on('click', function () {
        const startDateTime = $('input[name="startDateTime"]').val();
        const [startDate, startTime] = startDateTime.split('T');
        const endDateTime = $('input[name="endDateTime"]').val();
        const [endDate, endTime] = endDateTime.split('T');

        $.ajax({
            url: ctx.ajaxUrl + "filter",
            type: "GET",
            data: {
                startDate: startDate,
                endDate: endDate,
                startTime: startTime,
                endTime: endTime,
            },
            dataType: 'json',
            success: function (response) {
                $("#filterForm").modal("hide");
                ctx.datatableApi.clear().rows.add(response).draw();
            }
        });
    });
}
