let configuredCountdown = 0;
let countdownTime = configuredCountdown;
let totalTime = 0;
let interval = null;
let timerDisplay = document.getElementById('timer-display');

// WebSocket-Initialisierung mit automatischem Reconnect
let globalSocket = null;
let reconnectTimeout = null;

function initWebSocket(url, onMessage) {
  if (globalSocket) {
    try { globalSocket.close(); } catch (e) {}
  }
  globalSocket = new WebSocket(url);

  globalSocket.onopen = function() {
    console.log("WebSocket-Verbindung hergestellt");
    if (reconnectTimeout) {
      clearTimeout(reconnectTimeout);
      reconnectTimeout = null;
    }
  };

  globalSocket.onerror = function(error) {
    console.error("WebSocket-Fehler:", error);
  };

  globalSocket.onclose = function() {
    console.log("WebSocket-Verbindung geschlossen, versuche Reconnect...");
    reconnectTimeout = setTimeout(() => initWebSocket(url, onMessage), 2000);
  };

  globalSocket.onmessage = function(event) {
    if (onMessage) onMessage(event.data);
  };
}

// Zeitstand im localStorage speichern und wiederherstellen
function saveTime(key) {
  localStorage.setItem(key, countdownTime);
}

function loadTime(key, fallback) {
  const val = localStorage.getItem(key);
  return val !== null ? parseInt(val, 10) : fallback;
}

// --- Verbesserte Timer-Logik mit exakter Zeitmessung ---
const TIMER_KEY = 'matten_timer';
const TIMER_STATE_KEY = 'matten_timer_state';

// --- Dynamische Key-Generierung für Timer-Status pro Matte und Mattenrunde ---
function getTimerStateKey(matte, mattenrunde) {
  return `matten_timer_${matte}_${mattenrunde}_state`;
}

let currentMatte = null;
let currentMattenrunde = null;

function saveTimerState(state) {
  if (currentMatte && currentMattenrunde) {
    localStorage.setItem(getTimerStateKey(currentMatte, currentMattenrunde), JSON.stringify(state));
  }
}

function loadTimerState() {
  if (currentMatte && currentMattenrunde) {
    const val = localStorage.getItem(getTimerStateKey(currentMatte, currentMattenrunde));
    if (!val) return null;
    try { return JSON.parse(val); } catch { return null; }
  }
  return null;
}

function clearTimerState() {
  if (currentMatte && currentMattenrunde) {
    localStorage.removeItem(getTimerStateKey(currentMatte, currentMattenrunde));
  }
}

function getNow() {
  return Date.now();
}

let timerState = {
  isRunning: false,
  startTimestamp: null, // ms
  remainingTime: null   // ms
};

function updateTimerDisplay() {
  let displayTime;
  if (timerState.isRunning) {
    const elapsed = getNow() - timerState.startTimestamp;
    displayTime = timerState.remainingTime - elapsed;
    if (displayTime <= 0) {
      displayTime = 0;
      stop();
    }
  } else {
    displayTime = timerState.remainingTime;
  }
  timerDisplay.textContent = formatTime(displayTime);
  if (displayTime < 0) timerDisplay.classList.add('negative');
  else timerDisplay.classList.remove('negative');
}

function start() {
  if (timerState.isRunning) return;
  timerState.isRunning = true;
  timerState.startTimestamp = getNow();
  // Wenn vorher gestoppt, dann verbleibende Zeit weiter nutzen
  timerState.remainingTime = (typeof timerState.remainingTime === 'number') ? timerState.remainingTime : configuredCountdown;
  saveTimerState(timerState);
  updateTimerDisplay();
  if (!interval) {
    interval = setInterval(() => {
      updateTimerDisplay();
      saveTimerState(timerState);
    }, 10);
  }
}

function stop() {
  if (!timerState.isRunning) return;
  timerState.isRunning = false;
  // verbleibende Zeit berechnen
  const elapsed = getNow() - timerState.startTimestamp;
  timerState.remainingTime = Math.max(0, timerState.remainingTime - elapsed);
  timerState.startTimestamp = null;
  saveTimerState(timerState);
  updateTimerDisplay();
  clearInterval(interval);
  interval = null;
}

function reset() {
  timerState = {
    isRunning: false,
    startTimestamp: null,
    remainingTime: configuredCountdown
  };
  saveTimerState(timerState);
  updateTimerDisplay();
  clearInterval(interval);
  interval = null;
}

function init(countdown, matte, mattenrunde) {
  configuredCountdown = countdown;
  currentMatte = matte;
  currentMattenrunde = mattenrunde;
  const state = loadTimerState();
  if (state && typeof state.remainingTime === 'number') {
    timerState = state;
    // Wenn Timer läuft, Zeitverlust nachholen
    if (timerState.isRunning && timerState.startTimestamp) {
      const elapsed = getNow() - timerState.startTimestamp;
      timerState.remainingTime = Math.max(0, timerState.remainingTime - elapsed);
      timerState.startTimestamp = getNow();
      if (!interval) {
        interval = setInterval(() => {
          updateTimerDisplay();
          saveTimerState(timerState);
        }, 10);
      }
    }
  } else {
    timerState = {
      isRunning: false,
      startTimestamp: null,
      remainingTime: configuredCountdown
    };
    saveTimerState(timerState);
  }
  updateTimerDisplay();
}

const sendCommandToSocket = (command, socket) => {
  if (socket && socket.readyState === WebSocket.OPEN) {
    socket.send(command);
  } else {
    alert("Zeitfenster ist nicht verbunden.");
  }
}

const formatTime = (milliseconds) => {
  const totalSeconds = Math.floor(Math.abs(milliseconds) / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  const ms = Math.abs(milliseconds % 1000) / 10;
  return `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}.${ms < 10 ? '0' : ''}${Math.floor(ms)}`;
}
