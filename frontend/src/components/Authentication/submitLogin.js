
export async function submitLogin(credentials) {

  const request = {
    method: "POST",
    mode: "cors",
    headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': 'http://localhost' },
    //credentials: 'same-origin',
    body: JSON.stringify(credentials)
  };

  return fetch("http://localhost:8080/home/login", request)
    .then(response => response.ok ? response.json() : null)
    .then(body => body ? { status: true, id: body.sessionId, info: body.userInfo } : { status: false, id: null, info: null });
}