package com.vtaveira.domain.model;

public enum MessageStatus {
  PENDING, // Message is pending to be sent (e.g., waiting for receiver network availability)
  SENT, // Message has been sent but not yet delivered (no confirmation)
  DELIVERED, // Message has been delivered to the recipient's device (confirmation received)
  READ, // Message has been read by the recipient (read confirmation received)
  FAILED // Message failed to send or deliver (e.g., network issues, recipient offline)
}