// Chat state
let chatId = null;
let isWaitingForResponse = false;

// DOM elements
const chatForm = document.getElementById('chatForm');
const messageInput = document.getElementById('messageInput');
const chatMessages = document.getElementById('chatMessages');
const sendButton = chatForm.querySelector('.send-button');
const chatIdDisplay = document.getElementById('chatIdDisplay');
const chatIdInput = document.getElementById('chatIdInput');
const setChatIdBtn = document.getElementById('setChatIdBtn');
const chatIdInline = document.getElementById('chatIdInline');

// Initialize chat on page load
document.addEventListener('DOMContentLoaded', () => {
    initializeChat();
});

// Generate UUID v4
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// Initialize a new chat session
function initializeChat() {
    chatId = generateUUID();
    const params = new URLSearchParams(window.location.search);
    const debug = params.get('debug');
    if (debug === 'true') {
        // Show controls
        if (chatIdDisplay) chatIdDisplay.style.display = 'inline';
        if (chatIdInline) chatIdInline.style.display = 'flex';
        updateChatIdUI();
    } else {
        // Hide controls
        if (chatIdDisplay) chatIdDisplay.style.display = 'none';
        if (chatIdInline) chatIdInline.style.display = 'none';
    }
}

function updateChatIdUI() {
    if (chatIdDisplay) {
        chatIdDisplay.textContent = `(#${chatId})`;
    }
    if (chatIdInput) {
        chatIdInput.value = chatId;
    }
}

if (setChatIdBtn) {
    setChatIdBtn.addEventListener('click', async () => {
        const newId = (chatIdInput?.value || '').trim();
        if (!newId) {
            return;
        }
        chatId = newId;
        updateChatIdUI();
        // Load conversation for this chatId
        await loadConversation();
    });
}

async function loadConversation() {
    try {
        const res = await fetch(`/api/v1/chats/${chatId}`, {
            method: 'GET'
        });
        if (!res.ok) {
            addMessage('Failed to load conversation.', 'assistant');
            return;
        }
        const messages = await res.json(); // List<MessageDto>
        // Clear current messages
        chatMessages.innerHTML = '';
        // Render loaded messages
        for (const msg of messages) {
            const sender = msg.messageType === 'USER' ? 'user' : 'assistant';
            addMessage(msg.content, sender);
        }
    } catch (e) {
        console.error(e);
        addMessage('Error loading conversation.', 'assistant');
    }
}

// Handle form submission
chatForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (isWaitingForResponse || !messageInput.value.trim()) {
        return;
    }

    const userMessage = messageInput.value.trim();
    messageInput.value = '';

    // Add user message to chat
    addMessage(userMessage, 'user');

    // Show loading indicator
    const loadingElement = showLoading();

    // Disable input while waiting for response
    isWaitingForResponse = true;
    sendButton.disabled = true;
    messageInput.disabled = true;

    try {
        // Send message to backend using v1 API
        const response = await fetch(`/api/v1/chats/${chatId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                message: userMessage
            })
        });

        if (response.ok) {
            const assistantResponse = await response.text();

            // Remove loading indicator
            removeLoading(loadingElement);

            // Add assistant response
            addMessage(assistantResponse, 'assistant');
        } else {
            removeLoading(loadingElement);
            addMessage('Sorry, I encountered an error. Please try again.', 'assistant');
        }
    } catch (error) {
        console.error('Error sending message:', error);
        removeLoading(loadingElement);
        addMessage('Sorry, I could not connect to the server. Please try again.', 'assistant');
    } finally {
        // Re-enable input
        isWaitingForResponse = false;
        sendButton.disabled = false;
        messageInput.disabled = false;
        messageInput.focus();
    }
});

// Add a message to the chat
function addMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;

    const contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';

    const textContainer = document.createElement('div');
    textContainer.className = 'message-text';

    // Use innerHTML for assistant messages to support HTML formatting
    // Use textContent for user messages to prevent any injection
    if (sender === 'assistant') {
        textContainer.innerHTML = text;
    } else {
        textContainer.textContent = text;
    }

    const timeSpan = document.createElement('span');
    timeSpan.className = 'message-time';
    timeSpan.textContent = getCurrentTime();

    contentDiv.appendChild(textContainer);
    contentDiv.appendChild(timeSpan);
    messageDiv.appendChild(contentDiv);

    chatMessages.appendChild(messageDiv);
    scrollToBottom();
}

// Show loading indicator
function showLoading() {
    const loadingDiv = document.createElement('div');
    loadingDiv.className = 'message assistant-message';
    loadingDiv.id = 'loading-message';

    const loadingContent = document.createElement('div');
    loadingContent.className = 'loading-message';

    const dotsContainer = document.createElement('div');
    dotsContainer.className = 'loading-dots';

    for (let i = 0; i < 3; i++) {
        const dot = document.createElement('span');
        dotsContainer.appendChild(dot);
    }

    loadingContent.appendChild(dotsContainer);
    loadingDiv.appendChild(loadingContent);
    chatMessages.appendChild(loadingDiv);
    scrollToBottom();

    return loadingDiv;
}

// Remove loading indicator
function removeLoading(loadingElement) {
    if (loadingElement && loadingElement.parentNode) {
        loadingElement.parentNode.removeChild(loadingElement);
    }
}

// Get current time formatted
function getCurrentTime() {
    const now = new Date();
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
}

// Scroll to bottom of chat
function scrollToBottom() {
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Auto-focus on input
messageInput.focus();
