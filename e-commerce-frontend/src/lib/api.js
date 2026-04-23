const BASE_URL = 'http://localhost:8080/api';

async function request(path, { method = 'GET', body, token } = {}) {
	const headers = { 'Content-Type': 'application/json' };
	if (token) headers['Authorization'] = `Bearer ${token}`;

	const res = await fetch(`${BASE_URL}${path}`, {
		method,
		headers,
		body: body ? JSON.stringify(body) : undefined
	});

	const text = await res.text();
	const data = text ? JSON.parse(text) : null;

	if (!res.ok) {
		throw new Error(data?.message || 'Request failed');
	}

	return data;
}

export const api = {
	login: (body) => request('/auth/login', { method: 'POST', body }),
	register: (body) => request('/auth/register', { method: 'POST', body }),
	registerSeller: (body) => request('/auth/register/seller', { method: 'POST', body })
};
