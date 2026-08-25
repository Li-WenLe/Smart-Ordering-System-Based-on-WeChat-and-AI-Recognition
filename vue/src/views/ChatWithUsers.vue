<template>
    <div class="chat-app">
        <!-- 顶部标题栏 -->
        <header class="chat-header">
            <h1>用户会话</h1>
            <div class="connection-status" :class="connectionStatus">
                {{ connectionText }}
            </div>
        </header>

        <!-- 消息展示区域 -->
        <div class="message-container" ref="messageContainer">
            <div 
                v-for="(message, index) in messages" 
                :key="index"
                class="message"
                :class="{
                    'outgoing': message.sender === username,
                    'incoming': message.sender!== username
                }"
            >
                <div v-if="message.sender!== username" class="sender-name">
                    {{ message.sender }}
                </div>
                <div class="message-content">
                    {{ message.content }}
                </div>
                <div class="message-time">
                    {{ formatTime(message.timestamp) }}
                </div>
            </div>
        </div>

        <!-- 输入区域 -->
        <div class="input-area">
            <input
                v-model="inputMessage"
                @keyup.enter="sendMessage"
                placeholder="输入消息..."
                class="message-input"
            />
            <button @click="sendMessage" class="send-button">
                <i class="fas fa-paper-plane">发送</i>
            </button>
        </div>
    </div>
</template>

<script>

export default {
    name: 'ChatApp',
    data() {
        return {
            inputMessage: '',
            messages: [],
            socket: null,
            stompClient: null,
            connectionStatus: 'disconnected', // 'connected', 'connecting', 'error'
            reconnectAttempts: 0,
            maxReconnectAttempts: 5,
            username: 'admin', // 模拟用户名
            websocket:null
        }
    },
    computed: {
        connectionText() {
            const statusMap = {
                connected: '已连接',
                connecting: '连接中...',
                disconnected: '已断开',
                error: '连接错误'
            }
            return statusMap[this.connectionStatus]
        }
    },
    mounted() {
        this.initWebSocket()
        this.scrollToBottom()
    },
    beforeUnmount() {
        if (this.stompClient) {
            this.stompClient.deactivate();
        }
    },
    methods: {
        initWebSocket() {
            this.websocket = new WebSocket('ws://localhost:3000/chat');
            this.websocket.onopen = () => {
                console.log('WebSocket 连接成功');
                this.connectionStatus = 'connected';
                this.reconnectAttempts = 0;
            };
            this.websocket.onmessage = (event) => {
                console.log('收到消息：', event.data);
                const receivedMsg = JSON.parse(event.data);
                const newMessage = {
                    sender: receivedMsg.sender, // 如果没有发送者信息，可以设为默认值
                    content: receivedMsg.content,
                    timestamp: new Date().getTime() // 使用当前时间戳
                };
                
                this.messages.push(newMessage);
                this.scrollToBottom();
            };
            this.websocket.onclose = () => {
                console.log('WebSocket 连接关闭');
                this.connectionStatus = 'disconnected';
                this.attemptReconnect(); // 尝试重新连接
            };
            this.websocket.onerror = (error) => {
                console.log('WebSocket 连接错误：', error);
                this.connectionStatus = 'error';
                this.attemptReconnect(); // 尝试重新连接
            };
        },
        attemptReconnect() {
            if (this.reconnectAttempts < this.maxReconnectAttempts) {
                this.reconnectAttempts++;
                const delay = Math.min(1000 * this.reconnectAttempts, 10000);
                console.log(`将在${delay/1000}秒后尝试重新连接...`);
                setTimeout(() => {
                    this.initWebSocket();
                }, delay);
            }
        },
        sendMessage() {
            /*if (!this.inputMessage.trim()) return;

            if (this.stompClient && this.stompClient.connected) {
                this.messages.push(this.inputMessage);
                this.websocket.send(this.inputMessage)
                this.inputMessage = '';
                this.$nextTick(() => {
                    this.scrollToBottom();
                });
            } */
            const message = {
                sender: 'admin',
                content: this.inputMessage,
                timestamp: new Date().getTime()
            };
           this.websocket.send (JSON.stringify(message))
            // 本地也显示自己发送的消息
            this.inputMessage = '';
            this.scrollToBottom();
        },
        handleIncomingMessage(message) {
            this.messages.push(message);
            this.$nextTick(() => {
                this.scrollToBottom();
            });
        },
        scrollToBottom() {
            const container = this.$refs.messageContainer;
            if (container) {
                container.scrollTop = container.scrollHeight;
            }
        },
        formatTime(date) {
            return new Date(date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }
    }
}
</script>

<style scoped>
/* 样式保持不变 */
.chat-app {
    display: flex;
    flex-direction: column;
    height: 80vh;
    max-width: 800px;
    margin: 0 auto;
    background-color: #f5f7fa;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.chat-header {
    background-color: #4a6fa5;
    color: white;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.chat-header h1 {
    margin: 0;
    font-size: 1.2rem;
}

.connection-status {
    padding: 5px 10px;
    border-radius: 15px;
    font-size: 0.8rem;
}

.connection-status.connected {
    background-color: #4caf50;
}

.connection-status.connecting {
    background-color: #ff9800;
}

.connection-status.disconnected,
.connection-status.error {
    background-color: #f44336;
}

.message-container {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
    background-color: #e5e5e5;
}

.message {
    margin-bottom: 15px;
    max-width: 70%;
    position: relative;
}

.message-content {
    padding: 10px 15px;
    border-radius: 18px;
    font-size: 0.95rem;
    line-height: 1.4;
    word-break: break-word;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-time {
    font-size: 0.7rem;
    color: #666;
    margin-top: 4px;
}

.incoming {
    margin-right: auto;
}

.incoming.message-content {
    background-color: white;
    border-bottom-left-radius: 5px;
}

.incoming.message-time {
    text-align: left;
    padding-left: 10px;
}

.outgoing {
    margin-left: auto;
}

.outgoing.message-content {
    background-color: #dcf8c6;
    border-bottom-right-radius: 5px;
}

.outgoing.message-time {
    text-align: right;
    padding-right: 10px;
}

.sender-name {
    font-size: 0.8rem;
    color: #555;
    margin-bottom: 3px;
    padding-left: 10px;
}

.input-area {
    display: flex;
    padding: 15px;
    background-color: white;
    border-top: 1px solid #ddd;
}

.message-input {
    flex: 1;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 20px;
    outline: none;
    font-size: 0.95rem;
}

.message-input:focus {
    border-color: #4a6fa5;
}

.send-button {
    width: 45px;
    height: 45px;
    margin-left: 10px;
    border: none;
    border-radius: 50%;
    background-color: #4a6fa5;
    color: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.2s;
}

.send-button:hover {
    background-color: #3a5a8f;
}

/* 滚动条样式 */
.message-container::-webkit-scrollbar {
    width: 6px;
}

.message-container::-webkit-scrollbar-track {
    background: #f1f1f1;
}

.message-container::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
}

.message-container::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
}

/* 动画效果 */
.message {
    animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>