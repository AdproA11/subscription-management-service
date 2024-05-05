document.addEventListener('DOMContentLoaded', function() {
    fetchSubscriptions(); // Panggil fungsi fetchSubscriptions saat halaman dimuat

    // Tambahkan event listener untuk tombol Apply Filter
    var filterButton = document.getElementById('filter-button');
    filterButton.addEventListener('click', function() {
        fetchSubscriptionsStatus(); // Panggil fungsi fetchSubscriptionsStatus saat tombol ditekan
    });
});

function fetchSubscriptions() {
    const ownerUsername = "1";  // This should be dynamically set according to the logged-in user.
    fetch(`/api/subscriptions/user-subscriptions?ownerUsername=${ownerUsername}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(subscriptions => {
            const tbody = document.querySelector('#subscription-table tbody');
            tbody.innerHTML = '';  // Clear previous rows

            if (subscriptions.length > 0) {
                populateSubscriptionTable(subscriptions, tbody);
            }
        })
        .catch(error => {
            const tbody = document.querySelector('#subscription-table tbody');
            tbody.innerHTML = '';
            const row = tbody.insertRow();
            const cell = row.insertCell(0);
            cell.textContent = 'You haven’t subscribed to any boxes';
            cell.colSpan = 6;
            cell.style.textAlign = 'center';
        });
}

function fetchSubscriptionsStatus() {
    const selectedStatus = document.getElementById('status-filter').value;
    if (selectedStatus === 'All') {
        fetchSubscriptions(); // Panggil fetchSubscriptions jika status adalah "All"
        return;
    }
    fetch(`/api/subscriptions/user-subscriptions-status?status=${selectedStatus}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(subscriptions => {
            const tbody = document.querySelector('#subscription-table tbody');
            tbody.innerHTML = '';  // Clear previous rows

            if (subscriptions.length > 0) {
                populateSubscriptionTable(subscriptions, tbody);
            }
        })
        .catch(error => {
            const tbody = document.querySelector('#subscription-table tbody');
            tbody.innerHTML = '';
            const row = tbody.insertRow();
            const cell = row.insertCell(0);
            cell.textContent = 'Box dengan status '+selectedStatus+' tidak ditemukan';
            cell.colSpan = 6;
            cell.style.textAlign = 'center';
        });
}

function cancelSubscription(code) {
    fetch(`/api/subscriptions/unsubscribe?subscriptionCode=${code}`, {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                alert('Subscription cancelled successfully');
                fetchSubscriptions(); // Refresh the subscription list
            } else {
                alert('Failed to cancel subscription');
            }
        })
        .catch(error => {
            console.error('Error cancelling subscription:', error);
            alert('Failed to cancel subscription');
        });
}

function populateSubscriptionTable(subscriptions, tbody) {
    subscriptions.forEach(sub => {
        const row = tbody.insertRow();
        const nameCell = row.insertCell(0);
        const typeCell = row.insertCell(1);
        const codeCell = row.insertCell(2);
        const statusCell = row.insertCell(3);
        const priceCell = row.insertCell(4);
        const actionCell = row.insertCell(5);

        nameCell.textContent = sub.boxName;
        typeCell.textContent = sub.type;
        codeCell.textContent = sub.subscriptionCode;
        statusCell.textContent = sub.status;
        priceCell.textContent = `$${sub.total.toFixed(2)}`;

        if (sub.status !== 'Cancelled') {
            const cancelButton = document.createElement('button');
            cancelButton.textContent = 'Cancel Subscription';
            cancelButton.onclick = () => cancelSubscription(sub.subscriptionCode);
            actionCell.appendChild(cancelButton);
        }
    });
}
