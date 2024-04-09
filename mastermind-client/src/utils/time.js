export function timeSince(timestamp) {
  var seconds = Math.floor((new Date() - parseInt(timestamp)) / 1000);
  // case: if over a day
  var days = Math.floor(seconds / 86400);
  if (days > 1) {
    return "a while ago";
  }
  // case: hours ago
  var hours = Math.floor(seconds / 3600);
  if (hours > 2) {
    return hours + " hours ago";
  }
  if (hours > 1) {
    return "an hour ago";
  }
  // case: minutes
  var minutes = Math.floor(seconds / 60);
  if (minutes > 2) {
    return minutes + " minutes ago";
  }
  if (minutes > 1) {
    return "a minute ago";
  }
  return Math.floor(seconds) + " seconds ago";
}