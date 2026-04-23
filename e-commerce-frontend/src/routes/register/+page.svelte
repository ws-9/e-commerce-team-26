<script>
	import { goto } from '$app/navigation';
	import { auth } from '$lib/stores/auth.js';
	import { api } from '$lib/api.js';

	let role = $state('SHOPPER');
	let email = $state('');
	let password = $state('');
	let name = $state('');
	let address = $state('');
	let businessName = $state('');
	let description = $state('');
	let taxId = $state('');
	let contactEmail = $state('');
	let error = $state('');
	let loading = $state(false);

	async function handleSubmit(e) {
		e.preventDefault();
		loading = true;
		error = '';
		try {
			let data;
			if (role === 'SELLER') {
				data = await api.registerSeller({
					email,
					password,
					name,
					address,
					businessName,
					description,
					taxId,
					contactEmail
				});
			} else {
				data = await api.register({ email, password, name, address });
			}
			auth.setAuth(data.token, { email: data.email, name: data.name, role: data.role });
			goto('/dashboard');
		} catch (err) {
			error = err.message;
		} finally {
			loading = false;
		}
	}
</script>

<div class="min-h-screen flex items-center justify-center bg-gray-50 py-10">
	<div class="w-full max-w-lg bg-white rounded-2xl shadow-md p-8">
		<h1 class="text-2xl font-bold text-gray-800 mb-6">Create account</h1>

		<div class="flex gap-3 mb-6">
			<button
				type="button"
				onclick={() => (role = 'SHOPPER')}
				class="flex-1 py-2 rounded-lg text-sm font-medium border transition-colors {role === 'SHOPPER'
					? 'bg-blue-600 text-white border-blue-600'
					: 'bg-white text-gray-700 border-gray-300 hover:border-blue-400'}"
			>
				I'm a Shopper
			</button>
			<button
				type="button"
				onclick={() => (role = 'SELLER')}
				class="flex-1 py-2 rounded-lg text-sm font-medium border transition-colors {role === 'SELLER'
					? 'bg-blue-600 text-white border-blue-600'
					: 'bg-white text-gray-700 border-gray-300 hover:border-blue-400'}"
			>
				I'm a Seller
			</button>
		</div>

		{#if error}
			<p class="mb-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg p-3">
				{error}
			</p>
		{/if}

		<form onsubmit={handleSubmit} class="space-y-4">
			<div>
				<label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
				<input
					id="email"
					type="email"
					bind:value={email}
					required
					class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
					placeholder="you@example.com"
				/>
			</div>

			<div>
				<label for="password" class="block text-sm font-medium text-gray-700 mb-1">
					Password <span class="text-gray-400 font-normal">(min. 8 characters)</span>
				</label>
				<input
					id="password"
					type="password"
					bind:value={password}
					required
					minlength="8"
					class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
					placeholder="••••••••"
				/>
			</div>

			<div>
				<label for="name" class="block text-sm font-medium text-gray-700 mb-1">Full name</label>
				<input
					id="name"
					type="text"
					bind:value={name}
					required
					class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
					placeholder="Jane Doe"
				/>
			</div>

			<div>
				<label for="address" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
				<input
					id="address"
					type="text"
					bind:value={address}
					required
					class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
					placeholder="123 Main St, City, State"
				/>
			</div>

			{#if role === 'SELLER'}
				<hr class="border-gray-200" />
				<p class="text-xs font-semibold text-gray-500 uppercase tracking-wide">Business details</p>

				<div>
					<label for="businessName" class="block text-sm font-medium text-gray-700 mb-1">
						Business name
					</label>
					<input
						id="businessName"
						type="text"
						bind:value={businessName}
						required
						class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
						placeholder="Acme Store"
					/>
				</div>

				<div>
					<label for="taxId" class="block text-sm font-medium text-gray-700 mb-1">Tax ID</label>
					<input
						id="taxId"
						type="text"
						bind:value={taxId}
						required
						class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
						placeholder="12-3456789"
					/>
				</div>

				<div>
					<label for="contactEmail" class="block text-sm font-medium text-gray-700 mb-1">
						Business contact email
					</label>
					<input
						id="contactEmail"
						type="email"
						bind:value={contactEmail}
						required
						class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
						placeholder="support@acme.com"
					/>
				</div>

				<div>
					<label for="description" class="block text-sm font-medium text-gray-700 mb-1">
						Description <span class="text-gray-400 font-normal">(optional)</span>
					</label>
					<textarea
						id="description"
						bind:value={description}
						rows="3"
						class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
						placeholder="Tell shoppers about your store…"
					></textarea>
				</div>
			{/if}

			<button
				type="submit"
				disabled={loading}
				class="w-full bg-blue-600 text-white rounded-lg py-2 text-sm font-medium hover:bg-blue-700 disabled:opacity-50 transition-colors"
			>
				{loading ? 'Creating account…' : 'Create account'}
			</button>
		</form>

		<p class="mt-6 text-center text-sm text-gray-500">
			Already have an account?
			<a href="/login" class="text-blue-600 hover:underline font-medium">Sign in</a>
		</p>
	</div>
</div>
