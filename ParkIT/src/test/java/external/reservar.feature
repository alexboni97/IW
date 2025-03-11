Feature: Reservar en ParkIT
  Pruebas para el flujo de login, búsqueda y reserva de parking en ParkIT

  Background:
    * configure driver = { type: 'chrome' }  # Configuramos Chrome como driver

  Scenario: Flujo completo de reserva de parking como usuario
    # Paso 1: Iniciar sesión reutilizando login_a
    Given driver baseUrl
    And call read('login.feature@login_b')  
    # Según tu ejemplo anterior, 'b' va a /admin 
    Then waitForUrl(baseUrl + '/user')      

    # Paso 2: Ir al mapa (ajusto asumiendo que desde /admin se accede)
    Given driver baseUrl + '/user/2'        
    When submit().click("{button}Buscar")
    Then waitForUrl(baseUrl + '/user/map')
    And match html('#content') contains 'Ha redireccionado correctamente al mapa'

    # Paso 3: Rellenar el formulario de búsqueda
    And input('#search_field', 'Calle de la piruleta')
    And input('#latitud_field', '')
    And input('#longitud_field', '')
    And input('#rango_field', '')
    And input('#fecha_entrada_field', '25062025')
    And input('#hora_entrada_field', '2304')
    And input('#fecha_salida_field', '26062025')
    And input('#hora_salida_field', '1400')
    When submit().click("{button}Buscar")
    Then waitForUrl(baseUrl + '/user/map')
    And match html('#content') contains 'Búsqueda de la calle piruleta.'

    # Paso 4: Reservar el parking
    When submit().click("{button}Reservar")
    Then waitForUrl(baseUrl + '/user/reserve/1')  
    # Asumo que hay una selección de vehículo
    When submit().click("{button}Selecciona")
    When submit().click("{button}Honda Civic (XYZ5678)")
    When submit().click("{button}Reservar")
    Then waitForUrl(baseUrl + '/user/confirm-reserve')
    And match html('#content') contains 'Reserva confirmada'  