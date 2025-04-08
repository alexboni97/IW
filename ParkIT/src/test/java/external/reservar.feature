Feature: Reservar en ParkIT
  Pruebas para el flujo de login, búsqueda y reserva de parking en ParkIT

  Background:
    * configure driver = { type: 'chrome' }  # Configuramos Chrome como driver

  Scenario: Flujo completo de reserva de parking como usuario
    # Paso 1: Iniciar sesión reutilizando login_b
    Given driver baseUrl
    And call read('login.feature@login_b')  
    And delay(5000)
    Then waitForUrl(baseUrl + '/user/map')

    # Paso 3: Rellenar el formulario de búsqueda
    And input('#latitude', '40.416775')
    And input('#longitude', '-3.703790')
    And input('#rangeValue', '5000')
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
    And waitFor("a[parkingId='1027']")
    When click("a[parkingId='1027']")
    Then waitForUrl(baseUrl + '/user/reserve/1027')  
    # Seleccionamos uno de los vehiculos que se ofrecen, por ejemplo el Honda
    And select('#vehicleId', '1125')
    When submit().click("{button}Seleccionar Plaza")
    And delay(2000)  
    When click("div[data-id='1075']")
    When submit().click("{button}Confirmar Selección")
    And delay(2000)  
    When submit().click("{button}Reservar")
    Then waitForUrl(baseUrl + '/user/confirm-reserve')
    And delay(5000)  
