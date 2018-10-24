# Gerenciador de templates - Interface web de utilização do [templates-broker](https://softwarepublico.gov.br/gitlab/ans/templates-broker)
O Gerenciador de Templates é o sistema responsável por gerenciar os modelos de documentos que são ofertados pelo [templates-broker](https://softwarepublico.gov.br/gitlab/ans/templates-broker), ele serve como uma espécie de IDE para edição dos modelos. O propósito do sistema é permitir que os usuários possam interagir diretamente com os modelos de documentos.

## Requisitos
- Código-fonte do Gerenciador de Templates pode ser baixado a partir do link [https://softwarepublico.gov.br/gitlab/ans/gerenciador-de-templates/tags](https://softwarepublico.gov.br/gitlab/ans/gerenciador-de-templates/tags)
- [Apache Maven](https://maven.apache.org/) para baixar as dependências e compilar o pacote.
- Servidor [JBoss EAP 7.0.4](https://developers.redhat.com/products/eap/download/) ou [Wildfly 10](http://wildfly.org/downloads/).
- [Templates-broker](https://softwarepublico.gov.br/gitlab/ans/templates-broker) implantado e configurado.
- Conexão com a internet para que o Maven acesse os repositórios hospedeiros das dependências.

## Procedimentos para instalação
### Criar e configurar os arquivos de propriedades no JBoss
O Gerenciador de Templates faz uso de dois arquivos de propriedades que ficam na pasta `<JBOSS_HOME>\ans\properties`, os arquivos necessários são `services.properties` e `ws-users.properties`. Abaixo as propriedades que se fazem necessárias nestes arquivos.

<table>
  <tr>
    <th>Arquivo</th>
    <th>Propriedade</th>
    <th>Descrição</th>
  </tr>
  <tr>
    <td>services.properties</td>
    <td>templates.broker.uri</td>
    <td>URL do templates-broker</td>
  </tr>
  <tr>
    <td rowspan="2">ws-users.properties</td>
    <td>templates.web.user</td>
    <td>Usuário utilizado para se autenticar no broker</td>
  </tr>
  <tr>
    <td>templates.web.password</td>
    <td>Senha do usuário utilizado para se autenticar no broker</td>
  </tr>
</table>

### Criar security-domain no JBoss 
É necessário que haja um security-domain registrado com o nome `ans-form-auth`, o mesmo pode utilizar um banco de dados[^1] ou o LDAP. É importante destacar que o sistema trabalha com autorização baseada em papéis(RBAC[^2]) e que os usuários precisam ter seus papéis atribuídos. No caso do Gerenciador de Templates os usuários precisam ter o papel `usuario_interno`, essa configuração pode ser alterada no `web.xml`. 
[^1]: https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/how_to_configure_identity_management/configuring_a_security_domain_to_use_a_database
[^2]: https://en.wikipedia.org/wiki/Role-based_access_control

### Implantar pacote gerado pelo Maven
Após a realização de todos os passos anteriores, teremos o JBoss pronto para receber o pacote do Gerenciador de Templates. O deploy pode ser feito de diversas maneiras e não é o foco desse manual. 

Para essa etapa é necessário ter o Maven instalado e configurado. Ao realizar o primeiro build devemos desabilitar os testes automatizados, os testes dependem de uma instância ativa e impedirão a geração do pacote.
