document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('subscribe-button').addEventListener('click', function() {
        const boxId = document.getElementById('box-id').value;
        const type = document.querySelector('input[name="subscription-type"]:checked').value;
        subscribe(boxId, type);
    });

    document.getElementById('unsubscribe-button').addEventListener('click', function() {
        const boxId = document.getElementById('box-id').value;
        unsubscribe(boxId);
    });
});

function subscribe(boxId, type) {
    fetch(`/api/subscription-boxes/subscribe/${boxId}?type=${type}`, { method: 'POST' })
        .then(response => response.text())
        .then(message => alert(message));
}

function unsubscribe(boxId) {
    fetch(`/api/subscription-boxes/unsubscribe/${boxId}`, { method: 'POST' })
        .then(response => response.text())
        .then(message => alert(message));
}
