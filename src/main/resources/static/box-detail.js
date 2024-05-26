document.addEventListener('DOMContentLoaded', fetchBoxDetails);

function fetchBoxDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    const boxId = urlParams.get('boxId');
    const userId = '1'; // Simulated user ID, replace with dynamic session ID as needed.

    fetch(`/api/subscription-box/${boxId}`)
        .then(handleResponse)
        .then(box => {
            updateBoxDetails(box);
            checkUserSubscription(boxId, userId);
        })
        .catch(error => {
            console.error('Error fetching box details:', error);
            alert('Failed to load box details.');
        });
}

function checkUserSubscription(boxId, ownerUsername) {
    fetch(`/api/subscriptions/user-subscriptions?ownerUsername=${ownerUsername}`)
        .then(handleResponse)
        .then(subscriptions => {
            const activeSubscription = subscriptions.find(sub => sub.boxId === parseInt(boxId) &&
                (sub.status === "Pending" || sub.status === "Approved"));
            if (activeSubscription) {
                updateSubscriptionStatus(activeSubscription);
            } else {
                prepareSubscriptionUI(boxId);
            }
        })
        .catch(error => {
            console.log('User may not be subscribed or there is an error:', error);
            prepareSubscriptionUI(boxId);
        });
}

function updateBoxDetails(box) {
    document.title = box.name;
    document.getElementById('box-name').textContent = box.name;
    document.getElementById('box-description').textContent = `Description: ${box.description}`;
    document.getElementById('box-price').textContent = `Price: $${box.price}`;
    updateItemList(box.items);
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

function updateSubscriptionStatus(subscription) {
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = '';  // Clear any previous message

    const statusMessage = document.createElement('p');
    statusMessage.innerHTML = `You have an ongoing subscription with status: `;

    const statusSpan = document.createElement('span');
    statusSpan.textContent = subscription.status;
    if (subscription.status === 'Pending') {
        statusSpan.classList.add('pending-status');
    } else if (subscription.status === 'Approved') {
        statusSpan.classList.add('approved-status');
    }

    statusMessage.appendChild(statusSpan);
    messageDiv.appendChild(statusMessage);

    const cancelMessage = document.createElement('p');
    cancelMessage.textContent = 'Cancel the current subscription to make a new subscription.';
    messageDiv.appendChild(cancelMessage);

    const typeMessage = document.createElement('p');
    typeMessage.textContent = `Subscription Type: ${subscription.type}`;
    messageDiv.appendChild(typeMessage);

    const codeMessage = document.createElement('p');
    codeMessage.textContent = `Subscription Code: ${subscription.subscriptionCode}`;
    messageDiv.appendChild(codeMessage);

    const mySubscriptionButton = document.createElement('button');
    mySubscriptionButton.textContent = 'My Subscription';
    mySubscriptionButton.onclick = () => window.location.href = 'my-subscription.html';
    messageDiv.appendChild(mySubscriptionButton);
}


function prepareSubscriptionUI(boxId) {
    const subscribeButton = document.getElementById('subscribe-button');
    subscribeButton.textContent = 'Subscribe';
    subscribeButton.onclick = () => subscribeBox(boxId);
}

function handleResponse(response) {
    if (!response.ok) throw new Error('Network response was not ok');
    return response.json();
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
            updateSubscriptionStatus(data);
        })
        .catch(error => {
            console.error('Error during subscription:', error);
            document.getElementById('message').textContent = 'Sorry, there\'s something wrong.';
        });
}

