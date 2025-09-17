import { shortFormat } from "./helper/local_date_time_formatter.js";
import { downloadStringAsFile } from "./helper/download_file.js";

$(document).ready(async function () {

    // Botão para baixar o Histórico completo de mensagens recebidas
    $("#btn-history").click(function (e) {
        e.preventDefault();
        // console.log("Exibindo Histórico completo");

        $.get("http://localhost:8080/api/v1/history")
            .done((history) => {
                // console.log(history);
                // Baixa as mensagens
                downloadStringAsFile(history, 'history.txt', 'text/plain');
            })
    })

    // Botão para parar o alarme caso ele tenha sido disparado
    $("#btn-turn-off-alarm").click(function (e) {
        e.preventDefault();

        $.post("http://localhost:8080/api/v1/mqtt/publish?topic=home/action/&message=alarm.off")
    })

    // Desliga/Liga o Alarme
    $("#alarm-turn-on").change(function (e) {
        e.preventDefault()

        if ($(this).is(":checked")) {
            $.post("http://localhost:8080/api/v1/mqtt/publish?topic=home/action/&message=turn-on")
                .fail((error) => console.log(error))

        } else {
            $.post("http://localhost:8080/api/v1/mqtt/publish?topic=home/action/&message=turn-off")
                .fail((error) => console.log(error))
        }
    })

    function fetchMessages() {
        $.get("http://localhost:8080/api/v1/mqtt")
            .done(function (resp) {
                $("#messages").empty();

                if (resp.message == null) {
                    $("#messages").append("Sem Atualizações");
                    return;
                }

                $("#alarm-state").removeClass("position-absolute translate-middle-y end-0 bottom-0 alarm-state-off mx-5 fw-semibold");
                $("#alarm-state").addClass("position-absolute translate-middle-y end-0 bottom-0 alarm-state-on mx-5 fw-semibold");
                $("#alarm-state").text("").text("Ligado");
                $("#alarm-turn-on").prop("checked", true)

                if (String(resp.message).toLocaleLowerCase().includes("alarme desligado")) {
                    $("#alarm-state").removeClass("position-absolute translate-middle-y end-0 bottom-0 alarm-state-on mx-5 fw-semibold");
                    $("#alarm-state").addClass("position-absolute translate-middle-y end-0 bottom-0 alarm-state-off mx-5 fw-semibold");
                    $("#alarm-state").text("").text("Desligado");
                    $("#alarm-turn-on").prop("checked", false)
                }
                
                // Formata a data-hora
                let formattedReceivedAt = shortFormat(new Date(resp.receivedAt));
                let now = new Date(resp.receivedAt);

                if (String(resp.message).toLocaleLowerCase().includes("movimento detectado")) {

                    $("#messages")
                        .append(`<li class="list-group-item">${formattedReceivedAt} | <span class='msg-alarm-danger'>${resp.message}<span></li>`);

                    $(".deactivate-alarm").removeClass("disabled")

                    return;
                }

                $("#messages")
                    .append(`<li class="list-group-item">${formattedReceivedAt} | <span class='msg-alarm'>${resp.message}<span></li>`);

                $(".deactivate-alarm").addClass("disabled")
            })
            .fail(function () {
                console.log("Falha ao enviar a requisição");
            });
    }

    fetchMessages();
    setInterval(fetchMessages, 2000);
});