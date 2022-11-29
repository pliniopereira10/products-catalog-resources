# Backend de product-catalog-resources

> Backend utilizando **padrão de camadas**(*Model/Repository/Service/DTO/Controller*) no estilo de **arquitetura REST**. Projeto visa demonstrar conhecimentos adqueridos para implantação de **web service REST**.  
>
> **API** criada utilizando recursos do ecossistema **Spring**, está com um ambiente configurado no **perfil de teste** acessando o banco de dados **H2**, usando gerenciador de dependência **Maven** e **Java 11** como linguagem.
>
> O **CRUD** faz operações básicas de:
>
> - Inserir novo recurso
> - Busca paginada de recurso
> - Busca de recurso por id
> - Atualizar recurso
> - Deletar recurso

## 💎 Modelo UML da entidade

![UML dscatalog](https://github.com/pliniopereira10/images/blob/main/imagens-uml/uml-dscatalog.png)

## 👣 Passo-a-Passo

### 1. Criação do projeto
**Spring Boot**, adicionando as seguintes dependências:

   * Spring Boot DevTools
   * Validation
   * Spring Data JPA
   * H2 Database
   * PostgreSQL Driver
   * Spring Web

### 2. Implementação das entidade de domínio
   * Serializable
   * Atributos básicos
   * Construtores
   * Getters e Setters
   * HashCode e Equals

### 3. Mapeamento
Objeto-relacional **JPA** na entidade.

### 4. Configuração
Arquivo `application.properties` para acesso ao ***ambiente de teste*** no **banco de dados H2**.

### 5. Criação
Arquivo `application-test.properties`  e configurado para o banco de dados **H2** com acesso em memória.

### 6. Database seeding
Scripts **SQL** para povoar o **banco de dados** no  arquivo `import.sql`.

### 7. Criação interface
JpaRepository para a entidade(*camada de acesso a dados* **interface extends JpaRepository** ).

### 8. Criação da camada DTO 
Responsável por carregar os dados entre o ***controlador Rest e Serviço***.

   - **Observações:**
     - Crio uma **sobrecarga** de **construtor** DTO passando como **parâmetro** uma entidade, a fim de facilitar instanciação de DTO com os dados da entidade. 

### 9. Criação da camada de serviços.

   - **Observações:**
     - método **save**(*recebe dto e salva na entidade, não é necessário salvar id, pois o banco de dados é autoincrementado*).
     - método **findAll**(*tipo Page já é uma stream()*).
     - método **findById**(*utilzar classe Optional e buscar objeto get()*)
     - método **update**(*getReferenceById()*)
     - método **delete**(*sem a notação `@Transactional` para capturar exceção que retrona do banco de dados*)

### 10. Criação da camada controller com os endpoints:

#### POST/

  * ***Observações para lembrar:***
    * `@RequestBody`
    * Header com o endereço da resposta.
    * `URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/[id}").buildAndExpand(dto.getId()).toUri();`
    * `created(uri) - status 201`

#### GET/

- **Observações para lembrar:**
  
  - Endpoint com busca paginada. `Page<T>`
  
  - `@RequestParam` utilizado para passar um dado opcional na url.
  
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
  
  - `PageRequest request = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);`
  
  - `Page<T> pages = service.findAllCategories(request);`
  
  - `ok() - status 200`

#### GET/{id}

- **Observações para lembrar:**
  - `@PathVariable`
  - `ok() - status 200`

#### PUT/{id}

- `@PathVariable` e `@RequestBody`
- `ok() - status 200`

#### DELETE/{id}

- **Observações para lembrar:**
  - `ResponseEntity<Void>`
  - `@PathVariable`
  - `noContent().build() - status 204`

## :dart: Tratando Exceções

Quando o servidor retorna um código de erro **(HTTP) 500**, indica que encontrou uma condição inesperada e que o impediu de atender à solicitação, esse código de status não é um erro do servidor e sim da solicitação do usuário.

Para isso temos o **status 404 - NOT FOUND** que indica que o servidor não conseguiu encontrar o recurso solicitado.

### Personalizando

1. Criei o pacote **exceptions**.

2. Criei uma classe herdando `extends RunTimeExceptions`, a fim de repassar uma mensagem personalizada.

   ```java
   public class ResourceNotFoundException extends RuntimeException {
	   private static final long serialVersionUID = 1L;
	
	   public ResourceNotFoundException(String message) {
		   super(message);
	   }
   }
   ```

3. Classe criada para **padronizar** retorno de de mensagem.

   ```java
   public class StandardErrorMessage implements Serializable {
	   private static final long serialVersionUID = 1L;
	
	   private Instant timestamp;
	   private Integer status;
	   private String error;
	   private String message;
	   private String path;
   }
   ```

4. Classe criada para **controlar** exceções e suas mensagens.

   ```java
   @ControllerAdvice
   public class ResourceExcepetionHandler {
   	
   	@ExceptionHandler(ResourceNotFoundException.class)
   	public ResponseEntity<StandardErrorMessage> resourceNotFound(ResourceNotFoundException e, HttpServletRequest r){
   		StandardErrorMessage message = new StandardErrorMessage();
   		message.setTimestamp(Instant.now());
   		message.setStatus(HttpStatus.NOT_FOUND.value());
   		message.setError("Resource not found");
   		message.setMessage(e.getMessage());
   		message.setPath(r.getRequestURI());
   		
   		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
   	}
   }

## :timer_clock: Dados para auditoria

Criei dois atributos para informar o momento de inserção dos dados e atualização no padrão UTC.

- `private Instant createdAT`
- `private Instant updatedAt`
- notação do JPA:
  - no atributo `@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")`
  - nos métodos : `@PrePersist` e `@PreUpdate`
