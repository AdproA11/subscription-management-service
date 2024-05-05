function fetchBoxes() {
    const keywords = document.getElementById('search-keywords').value;
    const minPrice = document.getElementById('min-price').value;
    const maxPrice = document.getElementById('max-price').value;

    let url = '/api/subscriptions?';
    const params = new URLSearchParams();

    if (keywords) params.append('keywords', keywords);
    if (minPrice) params.append('minPrice', minPrice);
    if (maxPrice) params.append('maxPrice', maxPrice);

    url += params.toString();

    fetch(url)
        .then(response => response.json())
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

// Call fetchBoxes on page load to display all boxes initially
document.addEventListener('DOMContentLoaded', fetchBoxes);
