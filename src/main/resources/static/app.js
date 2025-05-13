var stompClient = null;
var username = generateRandomUsername();
var roomId = "room-test-123"; // <-- Thay bằng cách bạn lấy room động nếu cần

function generateRandomUsername() {
    var adjectives = ["Quick", "Lazy", "Happy", "Sad", "Angry"];
    var nouns = ["Fox", "Dog", "Cat", "Mouse", "Bear"];
    var adjective = adjectives[Math.floor(Math.random() * adjectives.length)];
    var noun = nouns[Math.floor(Math.random() * nouns.length)];
    return adjective + noun + Math.floor(Math.random() * 1000);
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Lắng nghe tin nhắn trong phòng cụ thể
        stompClient.subscribe('/topic/room/' + roomId, function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function sendMessage() {
    var messageContent = document.getElementById('message-input').value;
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            roomId: roomId,
            sentAt: new Date().toISOString()
        };
        stompClient.send("/app/sendMessage/" + roomId, {}, JSON.stringify(chatMessage));
        document.getElementById('message-input').value = '';
    }
}


function showMessage(message) {
    var messagesDiv = document.getElementById('messages');
    var messageElement = document.createElement('div');
    messageElement.textContent = message.sender + ": " + message.content;
    messagesDiv.appendChild(messageElement);
}

// Kết nối WebSocket khi mở trang
connect();
