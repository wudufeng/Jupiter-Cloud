<request path='/product/price_quantity/update' description='Lazada更新库存'>
	<parameter key='app_key' value='${auth.appKey}' />
	<parameter key='access_token' value='${auth.accessToken}' />
	<parameter key='timestamp' value='${timestamp}' />
	<parameter key='sign_method' value='hmac' />
	<parameter key='payload'  >
		<value>
			<![CDATA[
			<#list p as u>
			<Request>
				<Product>
					<Skus>
						<Sku>
							<SellerSku>${u.platformSku}</SellerSku>
							<Quantity>${u.updateStock}</Quantity>
						</Sku>
					</Skus>
				</Product>
			</Request>
			</#list>
			]]>
		</value>
	</parameter>
</request>