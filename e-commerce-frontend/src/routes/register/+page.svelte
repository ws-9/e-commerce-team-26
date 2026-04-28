<script>
	import { resolve } from '$app/paths';
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';

	let email = $state('');
	let password = $state('');
	let name = $state('');
	let address = $state('');
	let error = $state('');
	let loading = $state(false);

	async function handleSubmit(e) {
		e.preventDefault();
		error = '';
		loading = true;
		try {
			await authStore.registerShopper(email, password, name, address);
			goto(resolve('/products'));
		} catch (err) {
			error = err.message || 'Registration failed';
		} finally {
			loading = false;
		}
	}
</script>

<div class="flex min-h-screen items-center justify-center bg-gray-50 py-8">
	<div class="w-full max-w-md rounded-xl bg-white p-8 shadow">
		<h1 class="mb-6 text-2xl font-bold text-gray-900">Create Account</h1>

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
					class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
				/>
			</div>
			<div>
				<label for="name" class="mb-1 block text-sm font-medium text-gray-700">Full Name</label>
				<input
					id="name"
					type="text"
					bind:value={name}
					required
					class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
				/>
			</div>
			<div>
				<label for="address" class="mb-1 block text-sm font-medium text-gray-700">Address</label>
				<input
					id="address"
					type="text"
					bind:value={address}
					required
					class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
				/>
			</div>

			<button
				type="submit"
				disabled={loading}
				class="w-full rounded-lg bg-blue-600 py-2 font-medium text-white hover:bg-blue-700 disabled:opacity-50"
			>
				{loading ? 'Creating account…' : 'Create Account'}
			</button>
		</form>

		<p class="mt-4 text-center text-sm text-gray-600">
			Already have an account?
			<a href={resolve('/login')} class="font-medium text-blue-600 hover:underline">Sign in</a>
		</p>
	</div>
</div>
