// Display All box
function fetchAllBoxes() {
    const url = '/api/subscription-box/all';

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const container = document.getElementById('box-list');
            container.innerHTML = ''; // Clear previous results
            if(data.length === 0) {
                container.innerHTML = '<p>No boxes available that match your criteria.</p>';
            } else {
                data.forEach(box => {
                    const div = document.createElement('div');
                    div.className = 'box';
                    div.innerHTML = `<h3>${box.name}</h3><a href="box-detail.html?boxId=${box.id}">View Details</a>`;
                    container.appendChild(div);
                });
            }
        })
        .catch(error => {
            console.error('Failed to fetch boxes:', error);
            document.getElementById('box-list').innerHTML = '<p>Error loading boxes.</p>';
        });
}

// Display Filtered Box
function fetchBoxes() {
    const keywords = document.getElementById('search-keywords').value;
    const minPriceInput = document.getElementById('min-price').value;
    const maxPriceInput = document.getElementById('max-price').value;

    // Default values
    const minPrice = minPriceInput ? parseFloat(minPriceInput) : 0;
    const maxPrice = maxPriceInput ? parseFloat(maxPriceInput) : 15000000;

    let url = '/api/subscription-box';
    const params = new URLSearchParams();

    if (keywords) params.append('keywords', keywords);
    if (!isNaN(minPrice)) params.append('minPrice', minPrice);
    if (!isNaN(maxPrice)) params.append('maxPrice', maxPrice);

    if (params.toString()) {
        url += '?' + params.toString();
    }

    console.log('Fetching URL:', url);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const container = document.getElementById('box-list');
            container.innerHTML = '';
            if (data.length === 0) {
                container.innerHTML = '<p>No boxes available that match your criteria.</p>';
            } else {
                data.forEach(box => {
                    const div = document.createElement('div');
                    div.className = 'box';
                    div.innerHTML = `<h3>${box.name}</h3><a href="box-detail.html?boxId=${box.id}">View Details</a>`;
                    container.appendChild(div);
                });
            }
        })
        .catch(error => {
            console.error('Failed to fetch boxes:', error);
            document.getElementById('box-list').innerHTML = '<p>Error loading boxes.</p>';
        });
}

// Call fetchAllBoxes on page load to display all boxes initially
document.addEventListener('DOMContentLoaded', fetchAllBoxes);
document.getElementById('search-button').addEventListener('click', fetchBoxes);
