<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Real Estate Assistant</title>
    <link rel="stylesheet" href="/css/chat.css">
</head>
<body>
<div class="chat-container">
    <div class="chat-header">
        <div class="header-info">
            <div class="assistant-avatar">🏠</div>
            <div class="header-text">
                <h2 class="chat-title">Real Estate Assistant <span id="chatIdDisplay" class="chat-id"
                                                                   style="display:none"></span></h2>
                <div class="chat-id-inline" id="chatIdInline" style="display:none">
                    <input type="text" id="chatIdInput" class="chat-id-input" placeholder="Chat ID (UUID)"/>
                    <button id="setChatIdBtn" class="set-chat-id-btn" title="Load chat">↻</button>
                </div>
                <span class="status">Online</span>
            </div>
        </div>
    </div>

    <div class="chat-messages" id="chatMessages">
        <div class="message assistant-message">
            <div class="message-content">
                <p>Hello! I'm your real estate assistant. I'm here to help you find your ideal property.</p>
                <p>Let me know what you're looking for - location, budget, property type, number of rooms, or any
                    specific amenities!</p>
                <span class="message-time">Just now</span>
            </div>
        </div>
    </div>

    <div class="chat-input-container">
        <form id="chatForm" class="chat-input-form">
            <input
                    type="text"
                    id="messageInput"
                    class="message-input"
                    placeholder="Type your message..."
                    autocomplete="off"
                    required
            />
            <button type="submit" class="send-button" title="Send">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 2L11 13M22 2l-7 20-4-9-9-4 20-7z"/>
                </svg>
            </button>
        </form>
    </div>
</div>

<script src="/js/chat.js"></script>
</body>
</html>
