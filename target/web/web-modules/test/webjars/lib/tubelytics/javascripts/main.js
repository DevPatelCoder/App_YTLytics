function initializeWebSocket(wsUrl) {
    const socket = new WebSocket(wsUrl);

    socket.onopen = function() {
        console.log('WebSocket connection established');
    };

    socket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        updateResults(data);
    };

    socket.onclose = function() {
        console.log('WebSocket connection closed');
    };

    socket.onerror = function(error) {
        console.error('WebSocket error:', error);
    };

    document.getElementById('searchForm').onsubmit = function(event) {
        event.preventDefault();
        const query = document.getElementById('searchQuery').value;
        socket.send(query);
    };
}

function updateResults(data) {
    const resultsContainer = document.getElementById('results-container');
    const notification = document.getElementById('refreshNotification');

    // Clear previous results
    resultsContainer.innerHTML = '';

    // Append new results
    data.videos.forEach(video => {
        const videoCard = document.createElement('div');
        videoCard.className = 'video-card';
        videoCard.innerHTML = `
            <img src="${video.thumbnailUrl}" alt="Video Thumbnail">
            <div class="video-info">
                <h4><a href="${video.videoUrl}" target="_blank">${video.title}</a></h4>
                <p>Channel: <a href="/channel/${video.channelId}" target="_blank">${video.channelTitle}</a></p>
                <p>Description: ${video.description}</p>
                <p><a href="/tags/${video.videoId}">Tags</a></p>
            </div>
        `;
        resultsContainer.appendChild(videoCard);
    });

    // Show notification
    notification.classList.add('show');
    setTimeout(() => notification.classList.remove('show'), 3000);
}