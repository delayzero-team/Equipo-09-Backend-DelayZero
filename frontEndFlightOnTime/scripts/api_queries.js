const URL_BASE = 'http://localhost:8080';

export async function getStatistics() {

    let divLoading = document.getElementById("loading");
    let divError = document.getElementById("error");
    let sectionStatisticsCards = document.getElementById("cards-container");
    let sectionStatisticsGraphicsCards = document.getElementById("cardsGraphics-container");

    divLoading.style.display = 'block';
    divError.style.display = 'none';
    sectionStatisticsCards.style.display = 'none';
    sectionStatisticsGraphicsCards.style.display = 'none';

    try {
        const response = await fetch(`${URL_BASE}/statistics`);

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

        divLoading.style.display = 'none';
        sectionStatisticsCards.style.display = 'block';
        sectionStatisticsGraphicsCards.style.display = 'block';

        return await response.json();
        
    } catch (error) {

        console.error('Error cargando datos:', error);
        divError.textContent = `⚠️ Error conectando con la API: ${error.message}. Asegúrate de que el servidor esté corriendo en ${URL_BASE}`;
        divError.style.display = 'block';
        divLoading.style.display = 'none';

        return null;
    }       
}

export async function sendData(dataFlight) {

    let information = document.getElementById("information");
    let divResultContainer = document.getElementById("result-container");

    information.innerHTML = "🔄 Consultando vuelo...";
    information.style.color = "#16417E";
    divResultContainer.style.display = 'none';

    try {
        
        const response = await fetch(`${URL_BASE}/prediction`, {
            method : 'POST',
            headers : {'Content-Type':'application/json'},
            body : JSON.stringify(dataFlight)
        });

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

        divResultContainer.style.display = 'block';

        return await response.json();

    } catch (error) {

        console.log('Error al realizar POST: ' + error);
        information.innerHTML = "⚠️ Tuvimos un error por nuestra parte, vuelve a intentarlo.";
        information.style.color = "#c33";

        return null;
    }
}