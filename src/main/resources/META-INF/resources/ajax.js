    function updateStatusElements(d) {
        $(".nice-count").html(d.NiceCount);
        $(".naughty-count").html(d.NaughtyCount);

        if(d.submission && d.submission.status == "NAUGHTY") {
            $("#add-naughty").attr("disabled", true);
            $("#add-nice").attr("disabled", false);
        } else if(d.submission && d.submission.status == "NICE") {
            $("#add-naughty").attr("disabled", false);
            $("#add-nice").attr("disabled", true);
        } else {
            $("#add-naughty").attr("disabled", false);
            $("#add-nice").attr("disabled", false);
        }
    }
    $(document).ready(function () {
        $('#clock').countdown('2020/12/25', function(event) {
            var $this = $(this).html(event.strftime(''
            + '<span>%w</span> weeks '
            + '<span>%d</span> days '
            + '<span>%H</span> hr '
            + '<span>%M</span> min '
            + '<span>%S</span> sec'));
        });
        setInterval(function(){
            $.ajax({ url: "/",
                context: document.body,
                success: function(){
                    $.ajax({
                        url: "/count",
                        type: "GET",
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                    }).done(function (d) {
                        updateStatusElements(d)
                    });
                }
            });
        }, 10000)

        <!-- init page load   -->
        $.ajax({ url: "/",
            context: document.body,
            success: function(){
                $.ajax({
                    url: "/count",
                    type: "GET",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                }).done(function (d) {
                    updateStatusElements(d)
                });
            }
        });


        $("#add-nice").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: "/nice",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
            }).done(function (d) {
                updateStatusElements(d)
            });
        });

        $("#add-naughty").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: "/naughty",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
            }).done(function (d) {
                updateStatusElements(d)
            });
        });

    })