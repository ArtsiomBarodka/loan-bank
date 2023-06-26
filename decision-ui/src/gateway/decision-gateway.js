const baseDecisionServiceUrl = "/api/v1/bank/decisions";

export function fetchDecision(requestBody) {
  return fetch(baseDecisionServiceUrl, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestBody),
  }).then((response) => handleResponse(response));

  function handleResponse(response) {
    return response.json().then((json) => {
      if (!response.ok) {
        return Promise.reject(json);
      }
      return json;
    });
  }
}
