import { sendData } from "./api_queries.js";

async function showPrediction() {

    const dataFlight = getInputs();
    const dataPrediction = await sendData(dataFlight);

    if (dataPrediction == null) {
        return;
    }

    const detailsFlight = `El Vuelo con fecha ${dataFlight.fechaPartidaVuelo} de la Aerolinea ${dataFlight.nombreAerolinea} 
        ${dataPrediction.prevision == "Puntual" ? "es" : "esta"} ${dataPrediction.prevision}.
        Con una probabilidad de ${dataPrediction.probabilidad}.`;
    

    document.getElementById("result-prevision").textContent = `Vuelo ${dataPrediction.prevision}`;
    document.getElementById("result-probability").textContent = `${dataPrediction.probabilidad}% ~ ${dataPrediction.probabilidad * 100}%`;
    document.getElementById("information").textContent = detailsFlight;
}

function getInputs() {

    const inputAirline = document.getElementById("nameAirline").value;
    const inputOrigin = document.getElementById("flightOrigin").value;
    const inputDestination = document.getElementById("flightDestination").value;
    const inputDate = document.getElementById("flightDate").value;
    const inputDistance = document.getElementById("distance").value;

    const dataUser = {
        nombreAerolinea : inputAirline,
        origenVuelo : inputOrigin,
        destinoVuelo : inputDestination,
        fechaPartidaVuelo : inputDate,
        distanciaKilometros : inputDistance
    }

    return dataUser;
}

let submitButto = document.querySelector(".form-container");

// Adiciona escuchador de evento para el Formulario.
submitButto.addEventListener("submit", function(event) {
    event.preventDefault();
    showPrediction();
});
