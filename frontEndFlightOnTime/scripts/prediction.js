import { sendData } from "./api_queries.js";

async function showPrediction() {

    const dataFlight = getInputs();
    const dataPrediction = await sendData(dataFlight);

    if (dataPrediction == null) {
        return;
    }

    const textPrediction = dataPrediction.prevision;
    const probabilityPrediction = dataPrediction.probabilidad.toFixed(2);

    const detailsFlight = `El Vuelo con fecha ${dataFlight.fechaPartidaVuelo} de la Aerolínea ${dataFlight.nombreAerolinea} 
        ${textPrediction == "Puntual" ? "es" : "esta"} ${textPrediction}.
        Con una probabilidad de ${probabilityPrediction * 100}%.`;
    

    document.getElementById("result-prevision").textContent = `Vuelo ${textPrediction}`;
    document.getElementById("result-probability").textContent = `${probabilityPrediction}% ~ ${(probabilityPrediction * 100)}%`;
    document.getElementById("information").textContent = detailsFlight;
}

function getInputs() {

    const selectAirline = document.getElementById("nameAirline").value;
    const inputOrigin = document.getElementById("flightOrigin").value;
    const inputDestination = document.getElementById("flightDestination").value;
    const inputDate = document.getElementById("flightDate").value;
    const inputDistance = document.getElementById("distance").value;

    const dataUser = {
        nombreAerolinea : selectAirline ,
        origenVuelo : inputOrigin,
        destinoVuelo : inputDestination,
        fechaPartidaVuelo : inputDate,
        distanciaKilometros : inputDistance
    }

    console.log(dataUser);

    return dataUser;
}

function addOptionsAirlines() {

    let arrayAirlines = ["AA", "AS", "B6", "DL", "EV", "F9", "HA", "MQ", "NK", "OO", "UA", "US", "VX", "WN"];

    addOptions("nameAirline", "option-airline", arrayAirlines);
}

function addOptionsAirportOrigin() {

    let arrayAirportsOrigin = ["ABE", "ABQ", "ABR", "ANC", "ATL", "ATW", "AUS", "BDL", "BFL", "BHM", "BJI",
        "BNA", "BOI", "BOS", "BQN", "BRO", "BUF", "BWI", "CHS", "CID", "CLE", "CLT", "CRP", "CVG", "CWA", 
        "DCA", "DEN", "DFW", "ERI", "EUG", "EWR", "FAI", "FAR", "FAT", "FLL", "GEG", "GRB", "GSO", "GSP",
        "HIB", "HNL", "IAD", "IAG", "IAH", "IDA", "IND", "JAN", "JAX", "JFK", "LAN", "LAS", "LAX", "LEX", 
        "LGA", "LIT", "MCI", "MCO", "MEM", "MFR", "MHT", "MIA", "MKE", "MSN", "MSP", "MSY", "OAJ", "OAK",
        "OKC", "OMA", "ONT", "ORD", "PBG", "PBI", "PDX", "PHL", "PHX", "PIA", "PIT", "PNS", "PSE", "PVD",
        "PWM", "RDM", "RDU", "RIC", "RNO", "ROC", "RSW", "SAT", "SAV", "SBA", "SBN", "SEA", "SFO", "SJU", 
        "SMF", "SLC", "SYR", "TPA", "TTN", "TUL", "TYR", "TYS", "VPS",
    ];

    addOptions("listAirportOrigin", "option-airport", arrayAirportsOrigin);
}

function addOptionsAirportDestination() {

    let arrayAirportsDestination = ["ANC", "ATL", "BDL", "BOS", "BQN", "BUF", "BWI", "CLT", "DCA", "DEN",
        "DFW", "DTW", "EWR", "FLL", "HNL", "HOU", "IAD", "IAH", "ITO", "JFK", "KOA", "LAX", "LGA",
        "LGB", "LIH", "MCI", "MCO", "MIA", "MSP", "MYR", "OAK", "OGG", "ORD", "PBI", "PDX", "PHL",
        "PHX", "SEA", "SFO", "SJC", "SJU", "SLC", "TPA", "TTN"
    ];

    addOptions("listAirportDestination", "option-airport", arrayAirportsDestination);
}

function addOptions(idNameList, nameClassOption, arrayOptions) {

    let mainContainerOptions = document.getElementById(idNameList);

    // Se crean cada una de las options:
    for (let i = 0; i < arrayOptions.length; i++) {

        let labelOption = document.createElement("option");
        labelOption.classList.add(nameClassOption);
        labelOption.innerText = arrayOptions[i];

        mainContainerOptions.appendChild(labelOption);
    }
}

let submitForm = document.querySelector(".form-container");

// Adiciona escuchador de evento para el Formulario.
submitForm.addEventListener("submit", function(event) {
    event.preventDefault();
    showPrediction();
});

// Se agregan las opciones de Aerolineas y Aeropuertos al Formulario.
addOptionsAirlines();
addOptionsAirportOrigin();
addOptionsAirportDestination();
