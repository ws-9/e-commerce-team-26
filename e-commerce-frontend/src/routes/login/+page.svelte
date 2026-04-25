<script>
	import { resolve } from '$app/paths';
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';

	let email = $state('');
	let password = $state('');
	let error = $state('');
	let loading = $state(false);

	function redirectByRole(role) {
		if (role === 'ADMIN') goto(resolve('/dashboard/admin'));
		else if (role === 'SELLER') goto(resolve('/dashboard/seller'));
		else goto(resolve('/products'));
	}

	async function handleSubmit(e) {
		e.preventDefault();
		error = '';
		loading = true;
		try {
			const { userRole } = await authStore.login(email, password);
			redirectByRole(userRole);
		} catch (err) {
			error = err.message || 'Login failed';
		} finally {
			loading = false;
		}
	}
</script>

<div class="flex min-h-screen items-center justify-center bg-gray-50">
	<div class="w-full max-w-md rounded-xl bg-white p-8 shadow">
		<h1 class="mb-6 text-2xl font-bold text-gray-900">Sign In</h1>

		{#if error}
			<div class="mb-4 rounded border border-red-200 bg-red-50 p-3 text-sm text-red-700">
				{error}
			</div>
		{/if}

		<form onsubmit={handleSubmit} class="space-y-4">
			<div>
				<label for="email" class="mb-1 block text-sm font-medium text-gray-700">Email</label>
				<input
					id="email"
					type="email"
					bind:value={email}
					required
					autocomplete="email"
					class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
				/>
			</div>
			<div>
				<label for="password" class="mb-1 block text-sm font-medium text-gray-700">Password</label>
				<input
					id="password"
					type="password"
					bind:value={password}
					required
					autocomplete="current-password"
					class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
				/>
			</div>
			<button
				type="submit"
				disabled={loading}
				class="w-full rounded-lg bg-blue-600 py-2 font-medium text-white hover:bg-blue-700 disabled:opacity-50"
			>
				{loading ? 'Signing in…' : 'Sign In'}
			</button>
		</form>

		<p class="mt-4 text-center text-sm text-gray-600">
			Don't have an account?
			<a href={resolve('/register')} class="font-medium text-blue-600 hover:underline">Register</a>
		</p>
	</div>
</div>
