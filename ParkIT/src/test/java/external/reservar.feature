Feature: Reservar en ParkIT
  Pruebas para el flujo de login, búsqueda y reserva de parking en ParkIT

  Background:
    * configure driver = { type: 'chrome' }  # Configuramos Chrome como driver

  Scenario: Flujo completo de reserva de parking como usuario
    # Paso 1: Iniciar sesión reutilizando login_a
    Given driver baseUrl
    And call read('login.feature@login_b')  
    And delay(5000)
    Then waitForUrl(baseUrl + '/user')      

    # Paso 2: Ir al mapa 
    Given driver baseUrl + '/user/2'  
    And delay(2000)      
    When click("{a}Buscar")
    Then waitForUrl(baseUrl + '/user/map')
    And delay(2000)

    # Paso 3: Rellenar el formulario de búsqueda
    And input('#address', 'Calle de la piruleta')
    And input('#latitude', '')
    And input('#longitude', '')
    And input('#range', '')
    And input('#startDate', '25062025')
    And input('#startTime', '2304')
    And input('#endDate', '26062025')
    And input('#endTime', '1400')
    And delay(2000)   
    When submit().click("{button}Buscar")
    And delay(2000)  
    Then waitForUrl(baseUrl + '/user/map')

    # Paso 4: Reservar el parking
    And delay(2000)  
    When click("{a}Reservar")
    Then waitForUrl(baseUrl + '/user/reserve/1')  
    # Seleccionamos uno de los vehiculos que se ofrecen, por ejemplo el Honda
    And waitFor('#vehiculo')
    And select('#vehiculo', '1')
    When submit().click("{button}Reservar")
    Then waitForUrl(baseUrl + '/user/my-reserves')