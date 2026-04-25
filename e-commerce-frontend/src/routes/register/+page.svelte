<script>
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';

	let role = $state('SHOPPER');
	let email = $state('');
	let password = $state('');
	let name = $state('');
	let address = $state('');
	let businessName = $state('');
	let description = $state('');
	let taxID = $state('');
	let contactEmail = $state('');
	let error = $state('');
	let loading = $state(false);

	async function handleSubmit(e) {
		e.preventDefault();
		error = '';
		loading = true;
		try {
			if (role === 'SHOPPER') {
				await authStore.registerShopper(email, password, name, address);
				goto('/products');
			} else {
				await authStore.registerSeller(email, password, name, address, {
					businessName,
					description,
					taxID,
					contactEmail
				});
				goto('/dashboard/seller');
			}
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

		<div class="mb-6">
			<p class="mb-2 text-sm font-medium text-gray-700">Account Type</p>
			<div class="flex gap-6">
				<label class="flex cursor-pointer items-center gap-2">
					<input type="radio" bind:group={role} value="SHOPPER" class="text-blue-600" />
					<span class="text-sm">Shopper</span>
				</label>
				<label class="flex cursor-pointer items-center gap-2">
					<input type="radio" bind:group={role} value="SELLER" class="text-blue-600" />
					<span class="text-sm">Seller</span>
				</label>
			</div>
		</div>

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

			{#if role === 'SELLER'}
				<div>
					<label for="businessName" class="mb-1 block text-sm font-medium text-gray-700"
						>Business Name</label
					>
					<input
						id="businessName"
						type="text"
						bind:value={businessName}
						required
						class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
					/>
				</div>
				<div>
					<label for="description" class="mb-1 block text-sm font-medium text-gray-700"
						>Business Description</label
					>
					<textarea
						id="description"
						bind:value={description}
						rows="3"
						class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
					></textarea>
				</div>
				<div>
					<label for="taxID" class="mb-1 block text-sm font-medium text-gray-700">Tax ID</label>
					<input
						id="taxID"
						type="text"
						bind:value={taxID}
						required
						class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
					/>
				</div>
				<div>
					<label for="contactEmail" class="mb-1 block text-sm font-medium text-gray-700"
						>Business Contact Email</label
					>
					<input
						id="contactEmail"
						type="email"
						bind:value={contactEmail}
						required
						class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
					/>
				</div>
			{/if}

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
			<a href="/login" class="font-medium text-blue-600 hover:underline">Sign in</a>
		</p>
	</div>
</div>
