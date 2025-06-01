
let configuredCountdown = 0;
let countdownTime = configuredCountdown;
let totalTime = 0;
let interval = null;
let timerDisplay = document.getElementById('timer-display');

const init = (countdown) => {
  configuredCountdown = countdownTime = countdown;
  console.log("Zeit: ", countdownTime);
  updateTimer();
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

const updateTimer = () => {
  let time = formatTime(countdownTime);
  if (countdownTime >= 0) {
    timerDisplay.textContent = time;
  } else {
    timerDisplay.textContent = time;
    timerDisplay.classList.add('negative');
  }
  countdownTime -= 10;
  totalTime += 10;
}

const start = () => {
  if (!interval) {
    interval = setInterval(updateTimer, 10);
  }
}

const stop = () => {
  clearInterval(interval);
  interval = null;
}

const reset = () => {
  stop();
  totalTime = 0;
  countdownTime = configuredCountdown;
  updateTimer();
}
