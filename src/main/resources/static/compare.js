document.addEventListener('DOMContentLoaded', fetchBoxDetails);

function fetchBoxDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    const boxId = urlParams.get('boxId');

    fetch(`/api/subscription-box/${boxId}`)
        .then(handleResponse)
        .then(box => updateBoxDetails(box))
        .catch(error => {
            console.error('Error fetching box details:', error);
            alert('Failed to load box details.');
        });
}

function handleResponse(response) {
    if (!response.ok) throw new Error('Network response was not ok');
    return response.json();
}

function updateBoxDetails(box) {
    document.title = box.name;
    document.getElementById('box-name').textContent = box.name;
    document.getElementById('box-description').textContent = `Description: ${box.description}`;
    document.getElementById('box-price').textContent = `Price: $${box.price}`;
    updateItemList(box.items);
    updateSubscriptionUI(box);
}

function updateItemList(items) {
    const itemList = document.getElementById('item-list');
    itemList.innerHTML = ''; // Clear previous items
    items.forEach(item => {
        const li = document.createElement('li');
        li.textContent = item.name;
        itemList.appendChild(li);
    });
}

function updateSubscriptionUI(box) {
    const subscribeButton = document.getElementById('subscribe-button');
    subscribeButton.textContent = box.isSubscribed ? 'Unsubscribe' : 'Subscribe';
    subscribeButton.onclick = () => {
        if (box.isSubscribed) {
            unsubscribe(box.subscriptionCode);
        } else {
            subscribeBox(box.id);
        }
    };
}

function subscribeBox(boxId) {
    const typeSelector = document.getElementById('subscription-type');
    const type = typeSelector.value;
    const requestBody = { userId: '1', type: type.toUpperCase() };
    fetch(`/api/subscriptions/${boxId}/subscribe`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (!response.ok) throw new Error('Subscription failed');
            return response.json();
        })
        .then(data => {
            document.getElementById('message').textContent = 'Your subscription is waiting for approval by admin.';
            document.getElementById('subscribe-button').textContent = 'Unsubscribe';
        })
        .catch(error => {
            console.error('Error during subscription:', error);
            document.getElementById('message').textContent = 'Sorry, there\'s something wrong.';
        });
}

function unsubscribe(code) {
    fetch(`/api/subscriptions/unsubscribe`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ subscriptionCode: code })
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('message').textContent = 'Your subscription has been cancelled.';
                document.getElementById('subscribe-button').textContent = 'Subscribe';
            } else {
                throw new Error('Unsubscription failed');
            }
        })
        .catch(error => {
            console.error('Error during unsubscription:', error);
            document.getElementById('message').textContent = 'Sorry, there\'s something wrong with the unsubscription.';
        });
}

