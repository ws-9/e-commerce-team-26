import { writable } from 'svelte/store';
import { browser } from '$app/environment';

const TOKEN_KEY = 'auth_token';
const USER_KEY = 'auth_user';

function createAuthStore() {
	const initial = browser
		? {
				token: localStorage.getItem(TOKEN_KEY),
				user: JSON.parse(localStorage.getItem(USER_KEY) || 'null')
			}
		: { token: null, user: null };

	const { subscribe, set } = writable(initial);

	return {
		subscribe,
		setAuth(token, user) {
			if (browser) {
				localStorage.setItem(TOKEN_KEY, token);
				localStorage.setItem(USER_KEY, JSON.stringify(user));
			}
			set({ token, user });
		},
		clearAuth() {
			if (browser) {
				localStorage.removeItem(TOKEN_KEY);
				localStorage.removeItem(USER_KEY);
			}
			set({ token: null, user: null });
		}
	};
}

export const auth = createAuthStore();
