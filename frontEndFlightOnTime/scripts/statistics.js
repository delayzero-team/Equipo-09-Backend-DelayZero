import {getStatistics} from "./api_queries.js";

async function showStatistics() {

    const dataStatistics = await getStatistics();

    if (dataStatistics == null) {
        return;
    }

    if (dataStatistics.totalPredicciones == 0) {
        noData();
        return;
    }

    // Stats generales
    document.getElementById('totalPredictions').textContent = dataStatistics.totalPredicciones;
    document.getElementById('totalPunctualFlights').textContent = dataStatistics.prediccionesPuntuales;
    document.getElementById('totalFlightsDalayed').textContent = dataStatistics.prediccionesRetrasadas;
    document.getElementById('percentageDelayed').textContent = dataStatistics.porcentajeRetrasados + '%';

    // Chart: Aerolíneas
    const airlinesDiv = document.getElementById('airlinesChart');
    airlinesDiv.innerHTML = '';
            
    if (dataStatistics.estadisticasPorAerolinea) {
        const aerolineas = dataStatistics.estadisticasPorAerolinea.sort((a, b) => b.porcentajeRetraso - a.porcentajeRetraso);
                
        aerolineas.forEach(aero => {
            airlinesDiv.innerHTML += crearBarra(aero.aerolinea, aero.porcentajeRetraso,`${aero.porcentajeRetraso.toFixed(1)}%`);
        });
    }

    // Chart: Aeropuertos
    const airportsDiv = document.getElementById('airportsChart');
    airportsDiv.innerHTML = '';
            
    if (dataStatistics.estadisticasPorOrigen) {
        const aeropuertos = dataStatistics.estadisticasPorOrigen.sort((a, b) => b.porcentajeRetraso - a.porcentajeRetraso)
            .slice(0, 10);
                
        aeropuertos.forEach(aero => {
            airportsDiv.innerHTML += crearBarra(aero.aeropuerto,aero.porcentajeRetraso,`${aero.porcentajeRetraso.toFixed(1)}%`);

        });
    }

    // Chart: Horas
    const hoursDiv = document.getElementById('hoursChart');
    hoursDiv.innerHTML = '';
            
    if (dataStatistics.estadisticasPorHora) {
        const horas = dataStatistics.estadisticasPorHora.sort((a, b) => a.hora - b.hora);          
                
        horas.forEach(hora => {
            const probabilidadPorcentaje = hora.probabilidadPromedioRetraso * 100;
                hoursDiv.innerHTML += crearBarra(`${hora.hora}`,probabilidadPorcentaje,`${probabilidadPorcentaje.toFixed(1)}%`);

        });
    }
}

function noData() {
    let sectionStatisticsCards = document.getElementById("cards-container");
    let sectionStatisticsGraphicsCards = document.getElementById("cardsGraphics-container");
    let divLoading = document.getElementById('error');

    sectionStatisticsCards.style.display = 'none';
    sectionStatisticsGraphicsCards.style.display = 'none';
    divLoading.style.display = "block";
    divLoading.innerText = 'No hay datos disponibles. Realiza algunas predicciones primero usando Consultar.';
}

function crearBarra(label, porcentaje, texto) {
    return `
        <div class="bar">
            <div class="bar-label">${label}</div>
            <div class="bar-container">
                <div class="bar-fill" style="width: ${porcentaje}%">${texto}</div>
            </div>
        </div>    
    `;
}

// Adiciona escuchador de evento para el Botón Actualizar Estadisticas.
document.getElementById("update-button").addEventListener('click', function () {
    showStatistics();
});

// Cargar datos al inicio
showStatistics();
        
// Auto-refresh cada 30 segundos
setInterval(showStatistics, 30000);
